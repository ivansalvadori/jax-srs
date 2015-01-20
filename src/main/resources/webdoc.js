var apiDoc = null;

var showDetails = function(supportedClassIndex){
    clearDetails();
    
	var supportedClass = apiDoc.supportedClasses[supportedClassIndex];
	
	if(supportedClass['@id']){
	    $('#supportedClassId').show();
	}
 
	$("#supportedClassId").text(supportedClass['@id']);

	if(supportedClass['@context']){
        $("#context").show();
    }	
	$('#context tbody > tr').remove();
	for(i in supportedClass['@context']){	
        $("#context tbody").append('<tr><th><span class="text-muted">'+ i +'</span></th><td><a href="#">'+ supportedClass['@context'][i] +'</s></td></tr>');
	}
	
	if(supportedClass.supportedProperties){
	    $('#supportedProperties').show();
	}
	$('#supportedProperties tbody > tr').remove();
    for(i in supportedClass.supportedProperties){
                
        readonly = "";
        if(supportedClass.supportedProperties[i]['readonly'] == true){
            readonly = '<span class="label label-success">ro</span> ';            
        }
        
        writeonly = "";
        if(supportedClass.supportedProperties[i]['writeonly'] == true){
            writeonly = '<span class="label label-success">wo</span> ';            
        }
        
        required = "";
        if(supportedClass.supportedProperties[i]['required'] == true){
            required = '<span class="label label-success">req</span> ';            
        }
        
        $("#supportedProperties tbody").append('<tr><th>'+ supportedClass.supportedProperties[i]['property'] +'</th><td><code>'+ supportedClass.supportedProperties[i]['@type'] +'</code></td><td>'+supportedClass.supportedProperties[i]['@id'] + '<td>'+ readonly + writeonly + required + '</td>' + '</tr>')
    }
    
    if(supportedClass.supportedConstants){
        $('#supportedConstants').show();
    }
    $('#supportedConstants tbody > tr').remove();
    for(i in supportedClass.supportedConstants){
        $("#supportedConstants tbody").append('<tr><th>'+ supportedClass.supportedConstants[i] + '</th></tr>')
    }
    
    if(supportedClass.supportedOperations){
        $('#supportedOperationsDiv').show();
    }
    $('#supportedOperations').empty();
    for(i in supportedClass.supportedOperations){
        var httpMethod = supportedClass.supportedOperations[i]['method'];
        var metodo = null;
        if(httpMethod == 'GET'){
            metodo = '<span class="label label-success">GET</span> ';
        }
        if(httpMethod == 'POST'){
            metodo = '<span class="label label-primary">POST</span> ';
        }
        if(httpMethod == 'PUT'){
            metodo = '<span class="label label-warning">PUT</span> ';
        }
        if(httpMethod == 'DELETE'){
            metodo = '<span class="label label-danger">DELETE</span> ';
        }

        var id = "";
        if(supportedClass.supportedOperations[i]['@id']){
            id =  '<small class="text-muted">@id: </small> <a href="#"> '+ supportedClass.supportedOperations[i]['@id'] +' </a> ';        
        }
                      
        var url = "";
        if(supportedClass.supportedOperations[i]['url']){
            url =  '<br><small class="text-muted">IRI: </small> <a href="#"> '+  supportedClass.supportedOperations[i]['url'] +' </a> ';  
        }
        
        var operationType = "";
        if(supportedClass.supportedOperations[i]['@type']){
            operationType =  '<small class="text-muted">@type: </small>'+  supportedClass.supportedOperations[i]['@type'] + " ";  
        }
        
        var mapping = "";
        if(supportedClass.supportedOperations[i]['mapping']){
        	mapping = '<small class="text-muted">mapping: </small>';
        }
        for(j in supportedClass.supportedOperations[i]['mapping']){
        	iriTemplateMapping = supportedClass.supportedOperations[i]['mapping'][j];
        	req = "optional";
        	if(iriTemplateMapping['required']){
        		req = '<b>required</b>'
        	}
        	mapping += '<a href="#">{'+iriTemplateMapping['variable'] + ': ' + '<code>' + iriTemplateMapping['@id'] + '</code> ' + req + '}</a> ';      	
        }
        
        var headers = "";
        if(supportedClass.supportedOperations[i]['headers']){
            headers = '<small class="text-muted">headers: </small>';
        }
        for(j in supportedClass.supportedOperations[i]['headers']){
            header = supportedClass.supportedOperations[i]['headers'][j];
            req = "optional";
            if(header['required']){
                req = '<b>required</b>'
            }
            headers += '<a href="#">{'+header['header'] + ': ' + '<code>' + header['@id'] + '</code> ' + req + '}</a> ';       
        }
        
        var expects = "";
        if(supportedClass.supportedOperations[i]['expects']){
        	expects = '<small class="text-muted">expects:</small> <a href="#" style="padding-right:5px;"> '+ supportedClass.supportedOperations[i]['expects'] +' </a> ';        	
        }
        
        var returns = "";
        if(supportedClass.supportedOperations[i]['returns']){
        	returns = '<small class="text-muted">returns:</small> <a href="#" style="padding-right:5px;"> '+ supportedClass.supportedOperations[i]['returns'] +' </a> ';        	
        }
        
        $("#supportedOperations").append('<div id="supportedOperation" style="padding-bottom:10px;">'+ metodo + operationType + id + url + '<br>'+ mapping  + '<div>' + headers  +'</div>' + '<div>' + expects +  returns + '</div>' +'</div>');
    }
    
    if(supportedClass.globalIriTemplateMapping){
        $('#globalIriTemplateMappingDiv').show();
    }
    $('#globalIriTemplateMapping').empty();
    var globalMapping = "";
    for(i in supportedClass.globalIriTemplateMapping){
    	iriTemplateMapping = supportedClass.globalIriTemplateMapping[i];
    	req = "optional";
    	if(iriTemplateMapping['required']){
    		req = '<b>required</b>'
    	}
    	globalMapping += '<a href="#">{'+iriTemplateMapping['variable'] + ': ' + '<code>' + iriTemplateMapping['@id'] + '</code> ' + iriTemplateMapping['range'] +':'+ req + '}</a>  <br>' ;
    }
    $("#globalIriTemplateMapping").append(globalMapping);
    
    if(supportedClass.globalHeaders){
        $('#globalHeadersDiv').show();
    }
    $('#globalHeaders').empty();
    var globalHeaders = "";
    for(i in supportedClass.globalHeaders){
        header = supportedClass.globalHeaders[i];
        req = "optional";
        if(header['required']){
            req = '<b>required</b>'
        }
        globalHeaders += '<a href="#">{'+header['header'] + ': ' + '<code>' + header['@id'] + '</code> ' + req + '}</a> ';
    }
    $("#globalHeaders").append(globalHeaders);
}

var clearDetails = function(){
    $('#context').hide();
    $('#supportedOperationsDiv').hide();
    $('#supportedProperties').hide();
    $('#supportedConstants').hide();
    $('#globalIriTemplateMappingDiv').hide();
    $('#globalHeadersDiv').hide();
    $('#supportedClassId').hide();
}

$(document).ready(function(){
    
    clearDetails();
   
    $.ajax({
        url: './',
        headers:{accept:'application/ld+json'},
        contentType: 'application/ld+json',
        success: function(data) { 
            apiDoc = data;
            for(i in apiDoc.supportedClasses){
                $('#supportedClasses').append('<li class="list-group-item"><a href="javascript:showDetails('+i+')">' + apiDoc.supportedClasses[i]['@id'] + '</a></li>');
            }
        }
    });
   
});    