var headers = ['id', 'date', 'time', 'facility', 'severity', 'host', 'message'];
var flag = false;
var message, host, severity, facility, timeFrom, timeTo, dateFrom, dateTo, idFrom, idTo, currentPage, columnToSort, sortUp=true;
currentPage = d3.select("#currentPage")[0][0].value;

//обновление переменных поиска в соответствии с ввденными данными
function updateValues() {
    updateEmptyFieldsDateTime();
    message     = d3.select("#MessageValue")    [0][0].value;
    host        = d3.select("#HostValue")       [0][0].value;
    severity    = d3.select("#SiverityValue")   [0][0].value;
    switch(severity){
        case "Emergency: system is unusable":{
            severity = 0;
            break;
        }
        case "Alert: action must be taken immediately":{
            severity = 1;
            break;
        }
        case "Critical: critical conditions":{
            severity = 2;
            break;
        }
        case "Error: error conditions":{
            severity = 3;
            break;
        }
        case "Warning: warning conditions":{
            severity = 4;
            break;
        }
        case "Notice: normal but significant condition":{
            severity = 5;
            break;
        }
        case "Informational: informational messages":{
            severity = 6;
            break;
        }
        case "Debug: debug-level messages":{
            severity = 7;
            break;
        }
    }
    facility    = d3.select("#FacilityValue")   [0][0].value;
    switch(facility){
        case "kernel messages":{
            facility=0;
            break;
        }
            case "user-level messages":{
            facility=1;
            break;
        }
            case "mail system":{
            facility=2;
            break;
        }
            case "system daemons":{
            facility=3;
            break;
        }
            case "security/authorization messages":{
            facility=4;
            break;
        }
            case "messages generated internally by syslog":{
            facility=5;
            break;
        }
            case "line printer subsystem":{
            facility=6;
            break;
        }
            case "network news subsystem":{
            facility=7;
            break;
        }
            case "UUCP subsystem":{
            facility=8;
            break;
        }
            case "clock daemon":{
            facility=9;
            break;
        }
            case "security/authorization messages (note 2)":{
            facility=10;
            break;
        }
            case "FTP daemon":{
            facility=11;
            break;
        }
            case "NTP subsystem":{
            facility=12;
            break;
        }
            case "log audit":{
            facility=13;
            break;
        }
            case "log alert":{
            facility=14;
            break;
        }
            case "clock daemon (note 2)":{
            facility=15;
            break;
        }
            case "local use 0 (local0)":{
            facility=16;
            break;
        }
            case "local use 1 (local1)":{
            facility=17
            break;
        }
            case "local use 2 (local2)":{
            facility=18;
            break;
        }
            case "local use 3 (local3)":{
            facility=19;
            break;
        }
            case "local use 4 (local4)":{
            facility=20;
            break;
        }
            case "local use 5 (local5)":{
            facility=21;
            break;
        }
            case "local use 6 (local6)":{
            facility=22;
            break;
        }
            case "local use 7 (local7)":{
            facility=23;
            break;
        }
    }
    timeFrom    = d3.select("#TimeFromValue")   [0][0].value;
    timeTo      = d3.select("#TimeToValue")     [0][0].value;
    dateFrom    = d3.select("#DateFromValue")   [0][0].value;
    dateTo      = d3.select("#DateToValue")     [0][0].value;
    idFrom      = d3.select("#IdFromValue")     [0][0].value;
    idTo        = d3.select("#IdToValue")       [0][0].value;
    
}

function updateEmptyFieldsDateTime(){
    var d = new Date();
    var curr_date = d.getDate();
    var curr_month = d.getMonth() + 1;
    var curr_year = d.getFullYear();
    if(curr_month<10){
        curr_month="0"+curr_month;
    }
    if(curr_date<10){
        curr_date="0"+curr_date;
    }
    console.log(curr_year + "-" + curr_month + "-" + curr_date);

    if(d3.select("#TimeFromValue")[0][0].value.length==0){
        d3.select("#TimeFromValue")[0][0].value="00:00";
    }
    if(d3.select("#TimeToValue")[0][0].value.length==0){
        d3.select("#TimeToValue")[0][0].value="23:59";
    }
    if(d3.select("#DateFromValue")[0][0].value.length==0){
        d3.select("#DateFromValue")[0][0].value="1970-01-01";
    }
    if(d3.select("#DateToValue")[0][0].value.length==0){
        d3.select("#DateToValue")[0][0].value=curr_year + "-" + curr_month + "-" + curr_date;
    }
}
//обновление при листании вправо-влево постранично
function onChangeCurrentPage(left, right, leftMax, rightMax){
    var max = d3.select("#currentPageMax")[0][0].value;
    if(left){
        d3.select("#currentPage")[0][0].value=parseInt(d3.select("#currentPage")[0][0].value)-1;
    }
    if(right){
        d3.select("#currentPage")[0][0].value=parseInt(parseInt(d3.select("#currentPage")[0][0].value))+1;
    }
    if(leftMax){
        d3.select("#currentPage")[0][0].value=1;
    }
    if(rightMax){
        d3.select("#currentPage")[0][0].value=max;
    }
    if(max<JSON.parse(d3.select("#currentPage")[0][0].value)){
        d3.select("#currentPage")[0][0].value=max;
    }
    if(JSON.parse(d3.select("#currentPage")[0][0].value)<1){
        d3.select("#currentPage")[0][0].value=1;
    }
    currentPage = d3.select("#currentPage")[0][0].value;
    getData();
}

//возвращает строку поиска для get запроса
function getSearchStroke(){
    return  "message="      +message+    "&host="    +host+
            "&facility="    +facility+   "&severity="+severity+
            "&timeFrom="    +timeFrom+   "&timeTo"   +timeTo+
            "&dateFrom="    +dateFrom+   "&dateTo"   +dateTo+
            "&idFrom="      +idFrom+     "&idTo="    +idTo+
            "&pageNumber="  +currentPage+"&sort="    +columnToSort+
            "&sortUp="      +sortUp
    ;
}

//получение данных
function getData(){
    //удаляем табличку со старыми данными
    d3.select('body').select("#resultTable").select('table').remove();
    //обновляем переменные
    updateValues();
    //делаем запрос
    d3.json("http://91.151.187.30:1401/SyslogISS/logSearchGetData?"+getSearchStroke(), function (error,data) {
        //обновляем число страниц
        updateMaxPagesNumber();
        //создаем таблицу
        tabulate(data, headers); 
    });

}

//обновляем число страниц
function updateMaxPagesNumber(){
    d3.json("http://91.151.187.30:1401/SyslogISS/logSearchGetNumberOfPages?"+getSearchStroke(d3.select("#currentPage")[0][0].value), function (error,data) {
        d3.select("#currentPage").max=data[0];
        d3.select("#currentPageMax")[0][0].value=data[0];
    });
}

//создание таблицы
function tabulate(data, columns) {
        var table = d3.select('body').select("#resultTable").append('table');
        table.attr("class", "resultTable");
        var thead = table.append('thead');
		var	tbody = table.append('tbody');

		thead.append('tr')
          .attr("class", "resultTableHeader")
		  .selectAll('th')
		  .data(columns).enter()
		  .append('th')
          .on("click", function(columns){
            //columnToSort=columns;
          })
		  .text(function (column) { return column; })
          .on("click", function(column){
            if(columnToSort==column){
                sortUp=!sortUp;
            }else{
                columnToSort=column;
                sortUp=true;
            }
            getData();
          })
        ;

		// create a row for each object in the data
        var rows = tbody.selectAll('tr')
		  .data(data)
		  .enter()
		  .append('tr')
            
        ;

		// create a cell in each row for each column
		var cells = rows.selectAll('td')
		  .data(function (row) {
		    return columns.map(function (column) {
		      return {column: column, value: row[column]};
		    });
		  })
		  .enter()
		  .append('td')
        .attr("width", function(column){
        
            if(column.column=="message"){
                return "40%";
            }else{
                return "10%";
            }
        })
        .attr("clicked", false)
        .on("mouseover", function(d){
            d3.select(this).style("color", "black");
        })
        .on("mouseout", function(d){
            d3.select(this).style("color", "dimgrey");
        })
        .on("click", function(d){
            var str;
            var isClicked = d3.select(this).attr("clicked");
            console.log(isClicked);
            if(JSON.parse(isClicked)){
                console.log(1);
                var str;
                if(d.value.toString().length>100 || !isClicked){
                    str = d.value.toString().substr(0, 100) + "...";
                }
                if(d.value.toString().length<100 || !isClicked){
                    str = d.value.toString();
                }
                d3.select(this).text(str);
                d3.select(this).attr("clicked", false);
            }else{
                var str = d.value.toString();
                d3.select(this).text(str);
                
                d3.select(this).attr("clicked", true);
            }
            
            
        })
          .attr("class", "text")
          .text(function (d) { 
              if(d.value.toString().length>100){
                  var str = d.value.toString().substr(0, 100) + "...";
              }else{
                  var str = d.value.toString();
              }
              
            return str; 
         })
        
        
        
        ;

	  return table;
}


		