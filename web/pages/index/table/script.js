

var headers = ['id', 'date', 'time', 'facility', 'severity', 'host', 'message'];
var previosData;

d3.json("http://91.151.187.30:1401/SyslogISS/getLastLogs?limit=10", function (error,data) {
    previosData = data;
	tabulate(data, headers); 
});

var updateData = function updateData() {
    
    //table = d3.select('body').append('table');
    d3.json("http://91.151.187.30:1401/SyslogISS/getLastLogs?limit=10", function (error,data) {
        
        if(previosData!=data){
            d3.select('body').select('table').remove();
            tabulate(data, headers); 
            previosData=data;
        }
	   
    });
}


function tabulate(data, columns) {
        var table = d3.select('body').append('table');
        var thead = table.append('thead')
		var	tbody = table.append('tbody');
		// append the header row
		thead.append('tr')
		  .selectAll('th')
		  .data(columns).enter()
		  .append('th')
		    .text(function (column) { return column; });

		// create a row for each object in the data
        var rows = tbody.selectAll('tr')
		  .data(data)
		  .enter()
		  .append('tr');

		// create a cell in each row for each column
		var cells = rows.selectAll('td')
		  .data(function (row) {
		    return columns.map(function (column) {
		      return {column: column, value: row[column]};
		    });
		  })
		  .enter()
		  .append('td')
		    .text(function (d) { return d.value; });

	  return table;
	}


setInterval(updateData, 1000); 


		