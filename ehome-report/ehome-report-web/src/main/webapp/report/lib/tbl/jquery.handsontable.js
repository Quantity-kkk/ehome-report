/**
 * Handsontable 0.5.0
 * Handsontable is a simple jQuery plugin for editable tables with basic copy-paste compatibility with Excel and Google Docs
 *
 * Copyright 2012, Marcin Warpechowski
 * Licensed under the MIT license.
 * http://warpech.github.com/jquery-handsontable/
 */
/*jslint white: true, browser: true, plusplus: true, indent: 4, maxerr: 50 */
var cellCoords = {};
var dicData={};
var isThHander = false;
var isTHMenu = false;
var isChangeCell = false;
var isChangeWidth = false,isChangeHeight=false,isMoveDown = false;
var startCellCoord;
var changeCol,changeRow,etX,etY;
//var $tableModel = $('#tableModel');
//var $XMLModel = $('#dataSetsModel').val('<?xml version="1.0" encoding="UTF-8"?><dhccReport><dataSet></dataSet><body></body></hdccReport>');
var expData = {
		sum:new Array(),
		average:new Array(),
		count:new Array(),
		discount:new Array(),
		max:new Array(),
		min:new Array()
};
var sumData = {};
var averageData = {};
var countData = {};
var discountData = {};
var maxData = {};
var minData = {};
var Handsontable = { //class namespace
  extension: {}, //extenstion namespace
  helper: {}, //helper namespace
  tableStyle:{},
  XML:{},
  selection:{},
  expression:{},
  core:{}
};
var priv;
(function ($, window, Handsontable) {
  "use strict";
  Handsontable.Core = function (container, settings) {
    this.container = container;
    var $tableModel = $('#tableModel');
    var $dataSetsModel = $('#dataSetsModel');
    var $parmsModel = $('#parmsModel');
    var $parmsExtModel = $('#parmsExtModel');
    var $fieldModel = $('#fieldModel');
//    var $XMLModel = $('#XMLModel').val('<?xml version="1.0" encoding="UTF-8"?><dhccReport><dataSet></dataSet><body></body></dhccReport>');
    var datamap, grid, selection, editproxy, highlight,thdrafting, autofill, interaction, self = this;
    Handsontable.selection = selection;
    priv = {
      settings: {},
      isMouseOverTable: false,
      isMouseDown: false,
      isCellEdited: false,
      selStart: null,
      selEnd: null,
      editProxy: false,
      isPopulated: null,
      scrollable: null,
      hasLegend: null,
      lastAutoComplete: null,
      undoRedo: null,
      extensions: {},
      legendDirty: null
    };

    var lastChange = '';

    function isAutoComplete() {
      var typeahead = priv.editProxy.data("typeahead");
      if (typeahead && typeahead.$menu.is(":visible")) {
        return typeahead;
      }
      else {
        return false;
      }
    }

    /**
     * Measure the width and height of browser scrollbar
     * @return {Object}
     */
    function measureScrollbar() {
      var div = $('<div style="width:150px;height:150px;overflow:hidden;position:absolute;top:200px;left:200px"><div style="width:100%;height:100%;position:absolute">x</div>');
      $('body').append(div);
      var subDiv = $(div[0].firstChild);
      var w1 = subDiv.innerWidth();
      var h1 = subDiv.innerHeight();
      div[0].style.overflow = 'scroll';
      w1 -= subDiv.innerWidth();
      h1 -= subDiv.innerHeight();
      div.remove();
      return {width: w1, height: h1};
    }

    /**
     * Copied from bootstrap-typeahead.js for reference
     */
    function defaultAutoCompleteHighlighter(item) {
      var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&');
      return item.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
        return '<strong>' + match + '</strong>';
      })
    }

    var hasMinWidthProblem = ($.browser.msie && (parseInt($.browser.version, 10) <= 7));
    /**
     * Used to get over IE7 not respecting CSS min-width (and also not showing border around empty cells)
     * @param {Element} td
     */
    this.minWidthFix = function (td) {
      if (hasMinWidthProblem) {
        if (td.className) {
          td.innerHTML = '<div class="minWidthFix ' + td.className + '">' + td.innerHTML + '</div>';
        }
        else {
          td.innerHTML = '<div class="minWidthFix">' + td.innerHTML + '</div>';
        }
      }
    };

    var hasPositionProblem = ($.browser.msie && (parseInt($.browser.version, 10) <= 7));
    /**
     * Used to get over IE7 returning negative position in demo/buttons.html
     * @param {Object} position
     */
    this.positionFix = function (position) {
      if (hasPositionProblem) {
        if (position.top < 0) {
          position.top = 0;
        }
        if (position.left < 0) {
          position.left = 0;
        }
      }
    };

    /**
     * This will parse a delimited string into an array of arrays. The default delimiter is the comma, but this can be overriden in the second argument.
     * @see http://www.bennadel.com/blog/1504-Ask-Ben-Parsing-CSV-Strings-With-Javascript-Exec-Regular-Expression-Command.htm
     * @param strData
     * @param strDelimiter
     */
    var strDelimiter = '\t';
    var objPattern = new RegExp("(\\" + strDelimiter + "|\\r?\\n|\\r|^)(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|([^\"\\" + strDelimiter + "\\r\\n]*))", "gi");
    var dblQuotePattern = /""/g;

    function CSVToArray(strData) {
      var rows;
      if (strData.indexOf('"') === -1) { //if there is no " symbol, we don't have to use regexp to parse the input
        var r, rlen;
        rows = strData.split("\n");
        if (rows.length > 1 && rows[rows.length - 1] === '') {
          rows.pop();
        }
        for (r = 0, rlen = rows.length; r < rlen; r++) {
          rows[r] = rows[r].split("\t");
        }
      }
      else {
        rows = [
          []
        ];
        var arrMatches, strMatchedValue;
        while (arrMatches = objPattern.exec(strData)) {
          var strMatchedDelimiter = arrMatches[ 1 ];
          if (strMatchedDelimiter.length && (strMatchedDelimiter != strDelimiter)) {
            rows.push([]);
          }
          if (arrMatches[2]) {
            strMatchedValue = arrMatches[2].replace(dblQuotePattern, '"');
          }
          else {
            strMatchedValue = arrMatches[3];

          }
          rows[rows.length - 1].push(strMatchedValue);
        }
      }
      return rows;
    }

    datamap = {
      data: [],

      /**
       * Creates row at the bottom of the data array
       * @param {Object} [coords] Optional. Coords of the cell before which the new row will be inserted
       */
      createRow: function (coords) {
        var row = [];
        for (var c = 0; c < self.colCount; c++) {
          row.push('');
        }
        if (!coords || coords.row >= self.rowCount) {
          datamap.data.push(row);
        }
        else {
          datamap.data.splice(coords.row, 0, row);
        }
      },

      /**
       * Creates col at the right of the data array
       * @param {Object} [coords] Optional. Coords of the cell before which the new column will be inserted
       */
      createCol: function (coords) {
        var r = 0;
        if (!coords || coords.col >= self.colCount) {
          for (; r < self.rowCount; r++) {
            datamap.data[r].push('');
          }
        }
        else {
          for (; r < self.rowCount; r++) {
            datamap.data[r].splice(coords.col, 0, '');
          }
        }
      },

      /**
       * Removes row at the bottom of the data array
       * @param {Object} [coords] Optional. Coords of the cell which row will be removed
       * @param {Object} [toCoords] Required if coords is defined. Coords of the cell until which all rows will be removed
       */
      removeRow: function (coords, toCoords) {
        if (!coords || coords.row === self.rowCount - 1) {
          datamap.data.pop();
        }
        else {
          datamap.data.splice(coords.row, toCoords.row - coords.row + 1);
        }
      },

      /**
       * Removes col at the right of the data array
       * @param {Object} [coords] Optional. Coords of the cell which col will be removed
       * @param {Object} [toCoords] Required if coords is defined. Coords of the cell until which all cols will be removed
       */
      removeCol: function (coords, toCoords) {
        var r = 0;
        if (!coords || coords.col === self.colCount - 1) {
          for (; r < self.rowCount; r++) {
            datamap.data[r].pop();
          }
        }
        else {
          var howMany = toCoords.col - coords.col + 1;
          for (; r < self.rowCount; r++) {
            datamap.data[r].splice(coords.col, howMany);
          }
        }
      },

      /**
       * Returns single value from the data array
       * @param {Number} row
       * @param {Number} col
       */
      get: function (row, col) {
        return datamap.data[row] ? datamap.data[row][col] : null;
      },

      /**
       * Saves single value to the data array
       * @param {Number} row
       * @param {Number} col
       * @param {String} value
       */
      set: function (row, col, value) {
        datamap.data[row][col] = value;
      },

      /**
       * Clears the data array
       */
      clear: function () {
        for (var r = 0; r < self.rowCount; r++) {
          for (var c = 0; c < self.colCount; c++) {
            datamap.data[r][c] = '';
          }
        }
      },

      /**
       * Returns the data array
       * @return {Array}
       */
      getAll: function () {
        return datamap.data;
      },

      /**
       * Returns data range as array
       * @param {Object} start Start selection position
       * @param {Object} end End selection position
       * @return {Array}
       */
      getRange: function (start, end) {
        var r, rlen, c, clen, output = [], row;
        rlen = Math.max(start.row, end.row);
        clen = Math.max(start.col, end.col);
        for (r = Math.min(start.row, end.row); r <= rlen; r++) {
          row = [];
          for (c = Math.min(start.col, end.col); c <= clen; c++) {
            row.push(datamap.data[r][c]);
          }
          output.push(row);
        }
        return output;
      },

      /**
       * Return data as text (tab separated columns)
       * @param {Object} start (Optional) Start selection position
       * @param {Object} end (Optional) End selection position
       * @return {String}
       */
      getText: function (start, end) {
        var data = datamap.getRange(start, end), text = '', r, rlen, c, clen, stripHtml = /<(?:.|\n)*?>/gm;
        for (r = 0, rlen = data.length; r < rlen; r++) {
          for (c = 0, clen = data[r].length; c < clen; c++) {
            if (c > 0) {
              text += "\t";
            }
            if (data[r][c].indexOf('\n') > -1) {
              text += '"' + data[r][c].replace(stripHtml, '').replace(/"/g, '""') + '"';
            }
            else {
              text += data[r][c].replace(stripHtml, '');
            }
          }
          if (r !== rlen - 1) {
            text += "\n";
          }
        }
        text = text.replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&quot;/g, '"').replace(/&#039;/g, "'").replace(/&amp;/g, "&"); //unescape html special chars
        return text;
      }
    };

    grid = {
      /**
       * Alter grid
       * @param {String} action Possible values: "insert_row", "insert_col", "remove_row", "remove_col"
       * @param {Object} coords
       * @param {Object} [toCoords] Required only for actions "remove_row" and "remove_col"
       */
      alter: function (action, coords, toCoords) {
        var oldData, newData, changes, r, rlen, c, clen, result;
        oldData = $.extend(true, [], datamap.getAll());

        switch (action) {
          case "insert_row":
            datamap.createRow(coords);
            grid.createRow(coords);
            self.blockedCols.refresh();
            if (priv.selStart && priv.selStart.row >= coords.row) {
              priv.selStart.row = priv.selStart.row + 1;
            }
            if(isTHMenu){
            	selection.transformEnd(0, 0);
            }else if(selection.isMultiple()){
            	selection.transformEnd(1, 0); 
            }else{
            	selection.transformEnd(0, 0); //refresh selection, otherwise arrow movement does not work
            }
              
            break;

          case "insert_col":
            datamap.createCol(coords);
            grid.createCol(coords);
            self.blockedRows.refresh();
            if (priv.selStart && priv.selStart.col >= coords.col) {
              priv.selStart.col = priv.selStart.col + 1;
            }
            if(isTHMenu){
            	selection.transformEnd(0, 0);
            }else if(selection.isMultiple()){
            	selection.transformEnd(0, 1); 
            }else{
            	selection.transformEnd(0, 0); //refresh selection, otherwise arrow movement does not work
            }
            break;

          case "remove_row":
            datamap.removeRow(coords, toCoords);
            grid.removeRow(coords, toCoords);
            result = grid.keepEmptyRows();
            if (!result) {
              self.blockedCols.refresh();
            }
            selection.transformEnd(0, 0); //refresh selection, otherwise arrow movement does not work
            break;

          case "remove_col":
            datamap.removeCol(coords, toCoords);
            grid.removeCol(coords, toCoords);
            result = grid.keepEmptyRows();
            if (!result) {
              self.blockedRows.refresh();
            }
            selection.transformEnd(0, 0); //refresh selection, otherwise arrow movement does not work
            break;
        }

        changes = [];
        newData = datamap.getAll();
        for (r = 0, rlen = newData.length; r < rlen; r++) {
          for (c = 0, clen = newData[r].length; c < clen; c++) {
            changes.push([r, c, oldData[r] ? oldData[r][c] : null, newData[r][c]]);
          }
        }
        self.container.triggerHandler("datachange.handsontable", [changes, 'alter']);
        thdrafting.init();
      },

      /**
       * Creates row at the bottom of the <table>
       * @param {Object} [coords] Optional. Coords of the cell before which the new row will be inserted
       */
      createRow: function (coords) {
      	isChangeCell = true;
        var tr, c, r, td;
        tr = document.createElement('tr');
        self.blockedCols.createRow(tr);
        for (c = 0; c < self.colCount; c++) {
          tr.appendChild(td = document.createElement('td'));
          self.minWidthFix(td);
        }
        if (!coords || coords.row >= self.rowCount) {
          priv.tableBody.appendChild(tr);
          r = self.rowCount;
        }
        else {
          var oldTr = grid.getCellAtCoords(coords).parentNode;
          priv.tableBody.insertBefore(tr, oldTr);
          if(coords.row!=0){
          	for(var x=0;x<self.colCount;x++){
           	 if($(priv.tableBody.childNodes[coords.row-1].childNodes[x]).attr('rowspan') > 1){
              	$(priv.tableBody.childNodes[coords.row-1].childNodes[x]).attr('rowspan',parseInt($(priv.tableBody.childNodes[coords.row-1].childNodes[x]).attr('rowspan'))+1);
             	$(priv.tableBody.childNodes[coords.row].childNodes[x]).attr('class','merge_Hide');
              	$(priv.tableBody.childNodes[coords.row].childNodes[x]).css('display','none');
             }else if($(priv.tableBody.childNodes[coords.row-1].childNodes[x]).attr('colspan') > 1){
             	break;
             }else if($(priv.tableBody.childNodes[coords.row-1].childNodes[x]).css('display')=='none') {
             	$(priv.tableBody.childNodes[coords.row].childNodes[x]).attr('class','merge_Hide');
              	$(priv.tableBody.childNodes[coords.row].childNodes[x]).css('display','none');
              	for(var y = coords.row-1;y>=0;y--){
                	if($(priv.tableBody.childNodes[y].childNodes[x]).attr('rowspan') > 1){
                  		$(priv.tableBody.childNodes[y].childNodes[x]).attr('rowspan',parseInt($(priv.tableBody.childNodes[y].childNodes[x]).attr('rowspan'))+1);
                  		break;
                	}
             	}
             }
          	}
          }
		  for(var n=0;n<self.colCount;n++){
		  	for(var m=coords.row;m<self.rowCount+1;m++){
		  		var $td = $(grid.getCellAtCoords({row:m,col:n}));
		  		if($td.is(":hidden")){
		  			var name = $td.attr('name');
		  			if(!name){
		  				var $tdd = $(grid.getCellAtCoords({row:m+1,col:n}));
		  				$td.attr('name',$tdd.attr('name'));
		  				continue;
		  			}
		  			var topName = name.split(',');
		  			topName[0] = parseInt(topName[0]) + 23;
		  			if(coords.row<=topName[2].split('#')[0]){
		  				topName[2] = parseInt(topName[2].split('#')[0])+1+"#"+topName[2].split('#')[1];
		  			}
		  			name = topName;
		  			$td.attr('name',name);
		  		}
		  	}
		  }
		  
          r = coords.row;
        }
        self.rowCount++;
        for (c = 0; c < self.colCount; c++) {
          grid.updateLegend({row: r, col: c});
        }
     },

      /**
       * Creates col at the right of the <table>
       * @param {Object} [coords] Optional. Coords of the cell before which the new column will be inserted
       */
      createCol: function (coords) {
      	isChangeCell = true;
        var trs = priv.tableBody.childNodes, r, c, td;
        self.blockedRows.createCol();
        if (!coords || coords.col >= self.colCount) {
          for (r = 0; r < self.rowCount; r++) {
            trs[r].appendChild(td = document.createElement('td'));
            self.minWidthFix(td);
          }
          c = self.colCount;
        }
        else {
        	var flag = false;
          for (r = 0; r < self.rowCount; r++) {
            //合并列的首列
            if($(trs[r].childNodes[coords.col]).attr('colspan') && $(trs[r].childNodes[coords.col]).attr('colspan') > 1){
              $(trs[r].childNodes[coords.col]).attr('colspan',parseInt($(trs[r].childNodes[coords.col]).attr('colspan'))+1);
              trs[r].insertBefore(td = document.createElement('td'), grid.getCellAtCoords({row: r, col: coords.col}));
              $(trs[r].childNodes[coords.col + 1]).css('display','none');
              $(trs[r].childNodes[coords.col + 1]).attr('class','merge_Hide');
              //隐藏列
            } 
            else if($(trs[r].childNodes[coords.col+1]).attr('rowspan') > 1){
            	trs[r].insertBefore(td = document.createElement('td'), grid.getCellAtCoords({row: r, col: coords.col}));
            	flag = true;
            }
            else if($(trs[r].childNodes[coords.col+1]).css('display') == 'none'){
            	if(flag){
            		trs[r].insertBefore(td = document.createElement('td'), grid.getCellAtCoords({row: r, col: coords.col}));
            		continue;
            	}
              trs[r].insertBefore(td = document.createElement('td'), grid.getCellAtCoords({row: r, col: coords.col}));
              $(trs[r].childNodes[coords.col + 1]).css('display','none');
              $(trs[r].childNodes[coords.col + 1]).attr('class','merge_Hide');
              for(var i = coords.col; i >= 0;i-- ){
                if($(trs[r].childNodes[i]).attr('colspan') > 1){
                  $(trs[r].childNodes[i]).attr('colspan',parseInt($(trs[r].childNodes[i]).attr('colspan')) + 1);
                  break;
                }
              }
            } else {
              flag = false;
              trs[r].insertBefore(td = document.createElement('td'), grid.getCellAtCoords({row: r, col: coords.col}));
            }
            self.minWidthFix(td);
          }
          
          for(var n=coords.col;n<self.colCount+1;n++){
		  	for(var m=0;m<self.rowCount;m++){
		  		var $td = $(grid.getCellAtCoords({row:m,col:n}));
		  		if($td.is(":hidden")){
		  			var name = $td.attr('name');
		  			if(!name){
		  				var $tdd = $(grid.getCellAtCoords({row:m,col:n+1}));
		  				$td.attr('name',$tdd.attr('name'));
		  				continue;
		  			}
		  			var topName = name.split(',');
		  			topName[1] = parseInt(topName[1]) + 59;
		  			if(coords.col<=topName[2].split('#')[1]){
		  				topName[2] = topName[2].split('#')[0]+"#"+(parseInt(topName[2].split('#')[1])+1);
		  			}
		  			name = topName;
		  			$td.attr('name',name);
		  		}
		  	}
		  }
          
          c = coords.col;
        }
        self.colCount++;
        for (r = 0; r < self.rowCount; r++) {
          grid.updateLegend({row: r, col: c});
        }
      },

      /**
       * Removes row at the bottom of the <table>
       * @param {Object} [coords] Optional. Coords of the cell which row will be removed
       * @param {Object} [toCoords] Required if coords is defined. Coords of the cell until which all rows will be removed
       */
      removeRow: function (coords, toCoords) {
        if (!coords || coords.row === self.rowCount - 1) {
          $(priv.tableBody.childNodes[self.rowCount - 1]).remove();
          self.rowCount--;
        }
        else {
          for (var i = toCoords.row; i >= coords.row; i--) {
            //如果有合并单元格(删除的合并的首行)
            for(var x=0;x<$(priv.tableBody.childNodes[i]).find('td').length;x++){
              if($(priv.tableBody.childNodes[i].childNodes[x]).attr('rowspan') && $(priv.tableBody.childNodes[i].childNodes[x]).attr('rowspan') >1){
                $(priv.tableBody.childNodes[i+1].childNodes[x]).removeAttr('class');
                $(priv.tableBody.childNodes[i+1].childNodes[x]).removeAttr('style');
                $(priv.tableBody.childNodes[i+1].childNodes[x]).attr('rowspan',$(priv.tableBody.childNodes[i].childNodes[x]).attr('rowspan')-1);
                $(priv.tableBody.childNodes[i+1].childNodes[x]).attr('colspan',$(priv.tableBody.childNodes[i].childNodes[x]).attr('colspan'));
              } else {
                //如果是隐藏单元格
                if($(priv.tableBody.childNodes[i].childNodes[x]).css("display")=="none"){
                  for(var y= i; y>=0;y--){
                    if($(priv.tableBody.childNodes[y].childNodes[x]).attr('rowspan') &&  $(priv.tableBody.childNodes[y].childNodes[x]).attr('rowspan') >1){
                      $(priv.tableBody.childNodes[y].childNodes[x]).attr('rowspan',$(priv.tableBody.childNodes[y].childNodes[x]).attr('rowspan')-1);
                      break;
                    }
                  }
                }
              }
            }
		  		
            $(priv.tableBody.childNodes[i]).remove();
            self.rowCount--;
          }
          for(var n=0;n<self.colCount;n++){
            	for(var m=toCoords.row;m<self.rowCount;m++){
            		var $td = $(grid.getCellAtCoords({row:m,col:n}));
            		if($td.is(":hidden")){
		  				var name = $td.attr('name');
		  				var topName = name.split(',');
		  				topName[0] = parseInt(topName[0]) - 23;
		  				if(toCoords.row<topName[2].split('#')[0]){
		  					topName[2] = parseInt(topName[2].split('#')[0])-1+"#"+topName[2].split('#')[1];
		  				}
		  				name = topName;
		  				$td.attr('name',name);
		  			}
            	}
            }
        }
      },

      /**
       * Removes col at the right of the <table>
       * @param {Object} [coords] Optional. Coords of the cell which col will be removed
       * @param {Object} [toCoords] Required if coords is defined. Coords of the cell until which all cols will be removed
       */
      removeCol: function (coords, toCoords) {
        var trs = priv.tableBody.childNodes, colThs, i;
        if (self.blockedRows) {
          colThs = self.table.find('thead th');
        }
        var r = 0;
        if (!coords || coords.col === self.colCount - 1) {
          for (; r < self.rowCount; r++) {
            $(trs[r].childNodes[self.colCount + self.blockedCols.count() - 1]).remove();
            if (colThs) {
              colThs.eq(self.colCount + self.blockedCols.count() - 1).remove();
            }
          }
          self.colCount--;
        }
        else {
          for (; r < self.rowCount; r++) {
            for (i = toCoords.col; i >= coords.col; i--) {
              //如果有合并单元格(删除的合并的首列)
              if($(trs[r].childNodes[i + self.blockedCols.count()]).attr('colspan') &&  $(trs[r].childNodes[i + self.blockedCols.count()]).attr('colspan') > 1){
                $(trs[r].childNodes[i + self.blockedCols.count()+1]).removeAttr('class');
                $(trs[r].childNodes[i + self.blockedCols.count()+1]).removeAttr('style');
                $(trs[r].childNodes[i + self.blockedCols.count()+1]).attr('rowspan', $(trs[r].childNodes[i + self.blockedCols.count()]).attr('rowspan'));
                $(trs[r].childNodes[i + self.blockedCols.count()+1]).attr('colspan', $(trs[r].childNodes[i + self.blockedCols.count()]).attr('colspan')-1);
                $(trs[r].childNodes[i + self.blockedCols.count()]).remove();
              } else {
                //如果是隐藏单元格
                if($(trs[r].childNodes[i + self.blockedCols.count()]).css("display")=="none"){
                  for(var j=i + self.blockedCols.count();j>0;j--){
                    if( $(trs[r].childNodes[j]).attr('colspan') &&  $(trs[r].childNodes[j]).attr('colspan') > 1){
                      $(trs[r].childNodes[j]).attr('colspan', $(trs[r].childNodes[j]).attr('colspan')-1);
                      break;
                    } else if(!$(trs[r].childNodes[j]).css("display") == "none"){
                      break;
                    }
                  }
                  $(trs[r].childNodes[i + self.blockedCols.count()]).remove();
                } else {
                  $(trs[r].childNodes[i + self.blockedCols.count()]).remove();
                }

              }
            }
          }
          for(var n=toCoords.col;n<self.colCount;n++){
		  	for(var m=0;m<self.rowCount;m++){
		  		var $td = $(grid.getCellAtCoords({row:m,col:n}));
		  		if($td.is(":hidden")){
		  			var name = $td.attr('name');
		  			var topName = name.split(',');
		  			topName[1] = parseInt(topName[1]) - 59;
		  			if(toCoords.col<topName[2].split('#')[1]){
		  				topName[2] = topName[2].split('#')[0]+"#"+(parseInt(topName[2].split('#')[1])-1);
		  			}
		  			name = topName;
		  			$td.attr('name',name);
		  		}
		  	}
		  }
          
          if (colThs) {
            for (i = toCoords.col; i >= coords.col; i--) {
              colThs.eq(i + self.blockedCols.count()).remove();
            }
          }
          self.colCount -= toCoords.col - coords.col + 1;
        }
     },

      /**
       * Makes sure there are empty rows at the bottom of the table
       * @return recreate {Boolean} TRUE if row or col was added or removed
       */
      keepEmptyRows: function () {
        var rows, r, c, clen, emptyRows = 0, emptyCols = 0, rlen, recreateRows = false, recreateCols = false;

        var $tbody = $(priv.tableBody);

        //count currently empty rows
        rows = datamap.getAll();
        rlen = rows.length;
        rows : for (r = rlen - 1; r >= 0; r--) {
          for (c = 0, clen = rows[r].length; c < clen; c++) {
            if (rows[r][c] !== '') {
              break rows;
            }
          }
          emptyRows++;
        }

        //should I add empty rows to meet minSpareRows?
        if (self.rowCount < priv.settings.rows || emptyRows < priv.settings.minSpareRows) {
          for (; self.rowCount < priv.settings.rows || emptyRows < priv.settings.minSpareRows; emptyRows++) {
            datamap.createRow();
            grid.createRow();
            recreateRows = true;
          }
        }

        //should I add empty rows to meet minHeight
        //WARNING! jQuery returns 0 as height() for container which is not :visible. this will lead to a infinite loop
        if (priv.settings.minHeight) {
          if ($tbody.height() > 0 && $tbody.height() <= priv.settings.minHeight) {
            while ($tbody.height() <= priv.settings.minHeight) {
              datamap.createRow();
              grid.createRow();
              recreateRows = true;
            }
          }
        }

        //count currently empty cols
        rows = datamap.getAll();
        rlen = rows.length;
        if (rlen > 0) {
          clen = rows[0].length;
          cols : for (c = clen - 1; c >= 0; c--) {
            for (r = 0; r < rlen; r++) {
              if (rows[r][c] !== '') {
                break cols;
              }
            }
            emptyCols++;
          }
        }

        //should I add empty cols to meet minSpareCols?
        if (self.colCount < priv.settings.cols || emptyCols < priv.settings.minSpareCols) {
          for (; self.colCount < priv.settings.cols || emptyCols < priv.settings.minSpareCols; emptyCols++) {
            datamap.createCol();
            grid.createCol();
            recreateCols = true;
          }
        }

        //should I add empty rows to meet minWidth
        //WARNING! jQuery returns 0 as width() for container which is not :visible. this will lead to a infinite loop
        if (priv.settings.minWidth) {
          if ($tbody.width() > 0 && $tbody.width() <= priv.settings.minWidth) {
            while ($tbody.width() <= priv.settings.minWidth) {
              datamap.createCol();
              grid.createCol();
              recreateCols = true;
            }
          }
        }

        if (!recreateRows && priv.settings.enterBeginsEditing) {
          for (; ((priv.settings.rows && self.rowCount > priv.settings.rows) && (priv.settings.minSpareRows && emptyRows > priv.settings.minSpareRows) && (!priv.settings.minHeight || $tbody.height() - $tbody.find('tr:last').height() - 4 > priv.settings.minHeight)); emptyRows--) {
            grid.removeRow();
            datamap.removeRow();
            recreateRows = true;
          }
        }

        if (recreateRows && priv.selStart) {
          //if selection is outside, move selection to last row
          if (priv.selStart.row > self.rowCount - 1) {
            priv.selStart.row = self.rowCount - 1;
            if (priv.selEnd.row > priv.selStart.row) {
              priv.selEnd.row = priv.selStart.row;
            }
          } else if (priv.selEnd.row > self.rowCount - 1) {
            priv.selEnd.row = self.rowCount - 1;
            if (priv.selStart.row > priv.selEnd.row) {
              priv.selStart.row = priv.selEnd.row;
            }
          }
        }

        if (!recreateCols && priv.settings.enterBeginsEditing) {
          for (; ((priv.settings.cols && self.colCount > priv.settings.cols) && (priv.settings.minSpareCols && emptyCols > priv.settings.minSpareCols) && (!priv.settings.minWidth || $tbody.width() - $tbody.find('tr:last').find('td:last').width() - 4 > priv.settings.minWidth)); emptyCols--) {
            datamap.removeCol();
            grid.removeCol();
            recreateCols = true;
          }
        }

        if (recreateCols && priv.selStart) {
          //if selection is outside, move selection to last row
          if (priv.selStart.col > self.colCount - 1) {
            priv.selStart.col = self.colCount - 1;
            if (priv.selEnd.col > priv.selStart.col) {
              priv.selEnd.col = priv.selStart.col;
            }
          } else if (priv.selEnd.col > self.colCount - 1) {
            priv.selEnd.col = self.colCount - 1;
            if (priv.selStart.col > priv.selEnd.col) {
              priv.selStart.col = priv.selEnd.col;
            }
          }
        }

        if (recreateRows || recreateCols) {
          selection.refreshBorders();
          self.blockedCols.refresh();
          self.blockedRows.refresh();
        }

        return (recreateRows || recreateCols);
      },

      /**
       * Update legend
       */
      updateLegend: function (coords) {
        if (priv.settings.legend || priv.hasLegend) {
          var $td = $(grid.getCellAtCoords(coords));
          $td.removeAttr("style").removeAttr("title").removeData("readOnly");
          $td[0].className = '';
          $td.find("img").remove();
        }
        if (priv.settings.legend) {
          for (var j = 0, jlen = priv.settings.legend.length; j < jlen; j++) {
            var legend = priv.settings.legend[j],
                $img;
            if (legend.match(coords.row, coords.col, datamap.getAll)) {
              priv.hasLegend = true;
              typeof legend.style !== "undefined" && $td.css(legend.style);
              typeof legend.readOnly !== "undefined" && $td.data("readOnly", legend.readOnly);
              typeof legend.title !== "undefined" && $td.attr("title", legend.title);
              typeof legend.className !== "undefined" && $td.addClass(legend.className);
              if (typeof legend.icon !== "undefined" &&
                  typeof legend.icon.src !== "undefined" &&
                  typeof legend.icon.click !== "undefined") {
                $img = $('<img />').attr('src', legend.icon.src).addClass('icon');
                $img.on("click", (function (legend) {
                  return function (e) {
                    var func = legend.icon.click;
                    func.call(self, priv.selStart.row, priv.selStart.col, datamap.getAll, e.target);
                  }
                })(legend));
                $td.append($img);
              }
            }
          }
        }
      },

      /**
       * Is cell writable
       */
      isCellWritable: function ($td) {
        if (priv.isPopulated && $td.data("readOnly")) {
          return false;
        }
        return true;
      },

      /**
       * Populate cells at position with 2d array
       * @param {Object} start Start selection position
       * @param {Array} input 2d array
       * @param {Object} [end] End selection position (only for drag-down mode)
       * @param {Boolean} [allowHtml]
       * @param {String} [source="populateFromArray"]
       * @return {Object|undefined} ending td in pasted area (only if any cell was changed)
       */
      populateFromArray: function (start, input, end, allowHtml, source) {
        var r, rlen, c, clen, td, endTd, setData = [], current = {};
        rlen = input.length;
        if (rlen === 0) {
          return false;
        }
        current.row = start.row;
        current.col = start.col;
//      for (r = 0; r < rlen; r++) {
//        if ((end && current.row > end.row) || (!priv.settings.minSpareRows && current.row > self.rowCount - 1)) {
//          break;
//        }
//        current.col = start.col;
//        clen = input[r] ? input[r].length : 0;
//        for (c = 0; c < clen; c++) {
//          if ((end && current.col > end.col) || (!priv.settings.minSpareCols && current.col > self.colCount - 1)) {
//            break;
//          }
//          td = grid.getCellAtCoords(current);
//          if (grid.isCellWritable($(td))) {
//            setData.push([current.row, current.col, input[r][c], allowHtml]);
//          }
//          current.col++;
//          if (end && c === clen - 1) {
//            c = -1;
//          }
//        }
//        current.row++;
//        if (end && r === rlen - 1) {
//          r = -1;
//        }
//      }
		  for (r in input) {
          if ((end && current.row > end.row) || (!priv.settings.minSpareRows && current.row > self.rowCount - 1)) {
            break;
          }
          current.col = start.col;
          for (c in input[r]) {
            if ((end && current.col > end.col) || (!priv.settings.minSpareCols && current.col > self.colCount - 1)) {
              break;
            }
            td = grid.getCellAtCoords(current);
            if (grid.isCellWritable($(td))) {
              setData.push([current.row, current.col, input[r][c], allowHtml]);
            }
            current.col++;
            if (end && c === clen - 1) {
              c = -1;
            }
          }
          current.row++;
          if (end && r === rlen - 1) {
            r = -1;
          }
        }
        endTd = self.setDataAtCell(setData, null, null, null, source || 'populateFromArray');
        return endTd;
      },

      /**
       * Clears all cells in the grid
       */
      clear: function () {
        var tds = grid.getAllCells();
        for (var i = 0, ilen = tds.length; i < ilen; i++) {
          $(tds[i]).empty().attr('rowspan',null).attr('colspan',null).show();
          self.minWidthFix(tds[i]);
          grid.updateLegend(grid.getCellCoords(tds[i]));
        }
      },

      /**
       * Returns coordinates given td object
       */
      getCellCoords: function (td) {
        return {
          row: td.parentNode.rowIndex - self.blockedRows.count(),
          col: td.cellIndex - self.blockedCols.count()
        };
      },

      /**
       * Returns td object given coordinates
       */
      getCellAtCoords: function (coords) {
        if (coords.row < 0 || coords.col < 0) {
          return null;
        }
        var tr = priv.tableBody.childNodes[coords.row];
        if (tr) {
          return tr.childNodes[coords.col + self.blockedCols.count()];
        }
        else {
          return null;
        }
      },

      /**
       * Returns the top left (TL) and bottom right (BR) selection coordinates
       * @param {Object[]} coordsArr
       * @returns {Object}
       */
      getCornerCoords: function (coordsArr) {
        function mapProp(func, array, prop) {
          function getProp(el) {
            return el[prop];
          }

          if (Array.prototype.map) {
            return func.apply(Math, array.map(getProp));
          }
          return func.apply(Math, $.map(array, getProp));
        }

        return {
          TL: {
            row: mapProp(Math.min, coordsArr, "row"),
            col: mapProp(Math.min, coordsArr, "col")
          },
          BR: {
            row: mapProp(Math.max, coordsArr, "row"),
            col: mapProp(Math.max, coordsArr, "col")
          }
        };
      },

      /**
       * Returns array of td objects given start and end coordinates
       */
      getCellsAtCoords: function (start, end) {
        var corners = grid.getCornerCoords([start, end]);
        var r, c, output = [];
        for (r = corners.TL.row; r <= corners.BR.row; r++) {
          for (c = corners.TL.col; c <= corners.BR.col; c++) {
            output.push(grid.getCellAtCoords({
              row: r,
              col: c
            }));
          }
        }
        return output;
      },

      /**
       * Returns all td objects in grid
       */
      getAllCells: function () {
        var tds = [], trs, r, rlen, c, clen;
        trs = priv.tableBody.childNodes;
        rlen = self.rowCount;
        if (rlen > 0) {
          clen = self.colCount;
          for (r = 0; r < rlen; r++) {
            for (c = 0; c < clen; c++) {
              tds.push(trs[r].childNodes[c + self.blockedCols.count()]);
            }
          }
        }
        return tds;
      }
    };

    selection = {
      /**
       * Starts selection range on given td object
       * @param td element
       */
      setRangeStart: function (td) {
        selection.deselect();
        priv.selStart = grid.getCellCoords(td);
        selection.setRangeEnd(td);
      },

      /**
       * Ends selection range on given td object
       * @param {Element} td
       * @param {Boolean} [scrollToCell=true] If true, viewport will be scrolled to range end
       */
      setRangeEnd: function (td, scrollToCell) {
        var coords = grid.getCellCoords(td);
        selection.end(coords);
        if (!priv.settings.multiSelect) {
          priv.selStart = coords;
        }
        if (priv.settings.onSelection) {
          priv.settings.onSelection(priv.selStart.row, priv.selStart.col, priv.selEnd.row, priv.selEnd.col);
        }
        selection.refreshBorders();
        if (scrollToCell !== false) {
          highlight.scrollViewport(td);
        }
      },

      /**
       * Redraws borders around cells
       */
      refreshBorders: function () {
        if (!selection.isSelected()) {
          return;
        }
        if (priv.fillHandle) {
          autofill.showHandle();
        }
        priv.currentBorder.appear([priv.selStart]);
        highlight.on();
        editproxy.prepare();
        isChangeCell = false;
      },

      /**
       * Setter/getter for selection start
       */
      start: function (coords) {
        if (coords) {
          priv.selStart = coords;
        }
        return priv.selStart;
      },

      /**
       * Setter/getter for selection end
       */
      end: function (coords) {
        if (coords) {
          priv.selEnd = coords;
        }
        return priv.selEnd;
      },

      /**
       * Returns information if we have a multiselection
       * @return {Boolean}
       */
      isMultiple: function () {
        return !(priv.selEnd.col === priv.selStart.col && priv.selEnd.row === priv.selStart.row);
      },

      /**
       * Selects cell relative to current cell (if possible)
       */
      transformStart: function (rowDelta, colDelta, force) {
        if (priv.selStart.row + rowDelta > self.rowCount - 1) {
          if (force && priv.settings.minSpareRows > 0) {
            self.alter("insert_row", self.rowCount);
          }
          else if (priv.settings.autoWrapCol && priv.selStart.col + colDelta < self.colCount - 1) {
            rowDelta = 1 - self.rowCount;
            colDelta = 1;
          }
        }
        else if (priv.settings.autoWrapCol && priv.selStart.row + rowDelta < 0 && priv.selStart.col + colDelta >= 0) {
          rowDelta = self.rowCount - 1;
          colDelta = -1;
        }
        if (priv.selStart.col + colDelta > self.colCount - 1) {
          if (force && priv.settings.minSpareCols > 0) {
            self.alter("insert_col", self.colCount);
          }
          else if (priv.settings.autoWrapRow && priv.selStart.row + rowDelta < self.rowCount - 1) {
            rowDelta = 1;
            colDelta = 1 - self.colCount;
          }
        }
        else if (priv.settings.autoWrapRow && priv.selStart.col + colDelta < 0 && priv.selStart.row + rowDelta >= 0) {
          rowDelta = -1;
          colDelta = self.colCount - 1;
        }
        var td = grid.getCellAtCoords({
          row: (priv.selStart.row + rowDelta),
          col: priv.selStart.col + colDelta
        });
        if (td) {
          selection.setRangeStart(td);
        }
        else {
          selection.setRangeStart(grid.getCellAtCoords(priv.selStart)); //rerun some routines
        }
      },

      /**
       * Sets selection end cell relative to current selection end cell (if possible)
       */
      transformEnd: function (rowDelta, colDelta) {
      	isTHMenu = false;
        if (priv.selEnd) {
          var td = grid.getCellAtCoords({
            row: (priv.selEnd.row + rowDelta),
            col: priv.selEnd.col + colDelta
          });
          if (td) {
            selection.setRangeEnd(td);
          }
        }
      },

      /**
       * Returns true if currently there is a selection on screen, false otherwise
       * @return {Boolean}
       */
      isSelected: function () {
        var selEnd = selection.end();
        if (!selEnd || typeof selEnd.row === "undefined") {
          return false;
        }
        return true;
      },

      /**
       * Returns true if coords is within current selection coords
       * @return {Boolean}
       */
      inInSelection: function (coords) {
        if (!selection.isSelected()) {
          return false;
        }
        var sel = grid.getCornerCoords([priv.selStart, priv.selEnd]);
        return (sel.TL.row <= coords.row && sel.BR.row >= coords.row && sel.TL.col <= coords.col && sel.BR.col >= coords.col);
      },

      /**
       * Deselects all selected cells
       */
      deselect: function () {
        if (!selection.isSelected()) {
          return;
        }
        if (priv.isCellEdited) {
          editproxy.finishEditing();
        }
        highlight.off();
        priv.currentBorder.disappear();
        if (priv.fillHandle) {
          autofill.hideHandle();
        }
        selection.end(false);
        self.container.trigger('deselect.handsontable');
      },

      /**
       * Select all cells
       */
      selectAll: function () {
        if (!priv.settings.multiSelect) {
          return;
        }
        var tds = grid.getAllCells();
        if (tds.length) {
          selection.setRangeStart(tds[0]);
          selection.setRangeEnd(tds[tds.length - 1], false);
        }
      },
      

      /**
       * Deletes data from selected cells
       */
      empty: function () {
        if (!selection.isSelected()) {
          return;
        }
        var tds, i, ilen, changes = [], coords, old, $td;
        tds = grid.getCellsAtCoords(priv.selStart, selection.end());
        for (i = 0, ilen = tds.length; i < ilen; i++) {
          coords = grid.getCellCoords(tds[i]);
          old = datamap.get(coords.row, coords.col);
          $td = $(tds[i]);
          if (old !== '' && grid.isCellWritable($td)) {
            $td.empty();
            self.minWidthFix(tds[i]);
            datamap.set(coords.row, coords.col, '');
            changes.push([coords.row, coords.col, old, '']);
            grid.updateLegend(coords);
          }
        }
        if (changes.length) {
          self.container.triggerHandler("datachange.handsontable", [changes, 'empty']);
          setTimeout(function () {
            self.blockedRows.dimensions(changes);
            self.blockedCols.dimensions(changes);
          }, 10);
        }
        grid.keepEmptyRows();
        selection.refreshBorders();
      }
    };

    highlight = {
      /**
       * Create highlight border
       */
      init: function () {
        priv.selectionBorder = new Border(container, {
          className: 'selection',
          bg: true
        });
      },

      /**
       * Show border around selected cells
       */
      on: function () {
        if (!selection.isSelected()) {
          return false;
        }
        if (selection.isMultiple()) {
//      	if(isChangeCell){
//      		priv.selectionBorder.appear([priv.selStart, priv.selStart]);
//      	}else{
        		priv.selectionBorder.appear([priv.selStart, priv.selEnd]);
//      	}
        	
        }else {
          priv.selectionBorder.disappear();
        }
      },
	  
	  onCellBorder: function () {
        if (!selection.isSelected()) {
          return false;
        }
        if (selection.isMultiple()) {
        	var selStart={},selEnd={};
        	if(cellCoords.cellStart.row>cellCoords.cellEnd.row){
        		selStart.row = cellCoords.cellEnd.row;
        		selEnd.row = cellCoords.cellStart.row;
        	}else{
        		selStart.row = cellCoords.cellStart.row;
        		selEnd.row = cellCoords.cellEnd.row;
        	}
        	if(cellCoords.cellStart.col>cellCoords.cellEnd.col){
        		selStart.col = cellCoords.cellEnd.col;
        		selEnd.col = cellCoords.cellStart.col;
        	}else{
        		selStart.col = cellCoords.cellStart.col;
        		selEnd.col = cellCoords.cellEnd.col;
        	}
			var TLtd = self.findTDByhidden(grid.getCellAtCoords(selEnd));
			if($(TLtd).attr('rowspan')||$(TLtd).attr('rowspan')){
				var TLcoords = grid.getCellCoords(TLtd);
				var BR = {};
				BR.row = TLcoords.row+parseInt($(TLtd).attr('rowspan'))-1;
				BR.col = TLcoords.col+parseInt($(TLtd).attr('colspan'))-1;
				selEnd = BR;
			}
			var coords = self.onFor(selStart,selEnd);
			priv.selStart=coords.selStart;
			priv.selEnd=coords.selEnd;
			return {start:{row:coords.selStart.row,col:coords.selStart.col},end:{row:coords.selEnd.row,col:coords.selEnd.col}};
        }else {
          priv.selectionBorder.disappear();
          return {start:{row:cellCoords.cellStart.row,col:cellCoords.cellStart.col},end:{row:cellCoords.cellEnd.row,col:cellCoords.cellEnd.col}};
        }
      },
      /**
       * Hide border around selected cells
       */
      off: function () {
        if (!selection.isSelected()) {
          return false;
        }
        priv.selectionBorder.disappear();
      },

      /**
       * Scroll viewport to selection
       * @param td
       */
      scrollViewport: function (td) {
        if (!selection.isSelected()) {
          return false;
        }

        var $td = $(td);
        var tdOffset = $td.offset();
        var scrollLeft = priv.scrollable.scrollLeft(); //scrollbar position
        var scrollTop = priv.scrollable.scrollTop(); //scrollbar position
        var scrollOffset = priv.scrollable.offset();
        var rowHeaderWidth = self.blockedCols.count() ? $(self.blockedCols.main[0].firstChild).outerWidth() : 2;
        var colHeaderHeight = self.blockedRows.count() ? $(self.blockedRows.main[0].firstChild).outerHeight() : 2;

        var offsetTop = tdOffset.top;
        var offsetLeft = tdOffset.left;
        var scrollWidth, scrollHeight;
        if (scrollOffset) { //if is not the window
          scrollWidth = priv.scrollable.outerWidth();
          scrollHeight = priv.scrollable.outerHeight();
          offsetTop += scrollTop - scrollOffset.top;
          offsetLeft += scrollLeft - scrollOffset.left;
        }
        else {
          scrollWidth = priv.scrollable.width(); //don't use outerWidth with window (http://api.jquery.com/outerWidth/)
          scrollHeight = priv.scrollable.height();
        }
        scrollWidth -= priv.scrollbarSize.width;
        scrollHeight -= priv.scrollbarSize.height;

        var height = $td.outerHeight();
        var width = $td.outerWidth();

        if (scrollLeft + scrollWidth <= offsetLeft + width) {
          setTimeout(function () {
            priv.scrollable.scrollLeft(offsetLeft + width - scrollWidth);
          }, 1);
        }
        else if (scrollLeft > offsetLeft - rowHeaderWidth) {
          setTimeout(function () {
            priv.scrollable.scrollLeft(offsetLeft - rowHeaderWidth);
          }, 1);
        }

        if (scrollTop + scrollHeight <= offsetTop + height) {
          setTimeout(function () {
            priv.scrollable.scrollTop(offsetTop + height - scrollHeight);
          }, 1);
        }
        else if (scrollTop > offsetTop - colHeaderHeight) {
          setTimeout(function () {
            priv.scrollable.scrollTop(offsetTop - colHeaderHeight);
          }, 1);
        }
      }
    };

    autofill = {
      /**
       * Create fill handle and fill border objects
       */
      init: function () {
        if (!priv.fillHandle) {
          priv.fillHandle = new FillHandle(container);
          priv.fillBorder = new Border(container, {
            className: 'htFillBorder'
          });

          $(priv.fillHandle.handle).on('dblclick', autofill.selectAdjacent);
        }
        else {
          priv.fillHandle.disabled = false;
          priv.fillBorder.disabled = false;
        }
      },

      /**
       * Hide fill handle and fill border permanently
       */
      disable: function () {
        priv.fillHandle.disabled = true;
        priv.fillBorder.disabled = true;
      },

      /**
       * Selects cells down to the last row in the left column, then fills down to that cell
       */
      selectAdjacent: function () {
        var select, data, r, maxR, c;

        if (selection.isMultiple()) {
          select = priv.selectionBorder.corners;
        }
        else {
          select = priv.currentBorder.corners;
        }

        priv.fillBorder.disappear();

        data = datamap.getAll();
        rows : for (r = select.BR.row + 1; r < self.rowCount; r++) {
          for (c = select.TL.col; c <= select.BR.col; c++) {
            if (data[r][c]) {
              break rows;
            }
          }
          if (!!data[r][select.TL.col - 1] || !!data[r][select.BR.col + 1]) {
            maxR = r;
          }
        }
        if (maxR) {
          autofill.showBorder(grid.getCellAtCoords({row: maxR, col: select.BR.col}));
          autofill.apply();
        }
      },

      /**
       * Apply fill values to the area in fill border, omitting the selection border
       */
      apply: function () {
        var drag, select, start, end;

        priv.fillHandle.isDragged = 0;

        drag = priv.fillBorder.corners;
        if (!drag) {
          return;
        }

        priv.fillBorder.disappear();

        if (selection.isMultiple()) {
          select = priv.selectionBorder.corners;
        }
        else {
          select = priv.currentBorder.corners;
        }

        if (drag.TL.row === select.TL.row && drag.TL.col < select.TL.col) {
          start = drag.TL;
          end = {
            row: drag.BR.row,
            col: select.TL.col - 1
          };
        }
        else if (drag.TL.row === select.TL.row && drag.BR.col > select.BR.col) {
          start = {
            row: drag.TL.row,
            col: select.BR.col + 1
          };
          end = drag.BR;
        }
        else if (drag.TL.row < select.TL.row && drag.TL.col === select.TL.col) {
          start = drag.TL;
          end = {
            row: select.TL.row - 1,
            col: drag.BR.col
          };
        }
        else if (drag.BR.row > select.BR.row && drag.TL.col === select.TL.col) {
          start = {
            row: select.BR.row + 1,
            col: drag.TL.col
          };
          end = drag.BR;
        }

        if (start) {
          var inputArray = CSVToArray(priv.editProxy.val(), '\t');
//			var d = datamap.getText(select.TL,select.BR);
//			var inputArray = CSVToArray(d, '\t');
          grid.populateFromArray(start, inputArray, end, null, 'autofill');

          selection.setRangeStart(grid.getCellAtCoords(drag.TL));
          selection.setRangeEnd(grid.getCellAtCoords(drag.BR));
        }
        else {
          //reset to avoid some range bug
          selection.refreshBorders();
        }
      },
	  
      /**
       * Show fill handle
       */
      showHandle: function () {
      	if(isChangeCell){
      		priv.fillHandle.appear([priv.selStart, priv.selStart]);
      	}else{
      		priv.fillHandle.appear([priv.selStart, priv.selEnd]);
      	}
       
      },

      /**
       * Hide fill handle
       */
      hideHandle: function () {
        priv.fillHandle.disappear();
      },

      /**
       * Show fill border
       */
      showBorder: function (td) {
        var coords = grid.getCellCoords(td);
        var corners = grid.getCornerCoords([priv.selStart, priv.selEnd]);
        if (priv.settings.fillHandle !== 'horizontal' && (corners.BR.row < coords.row || corners.TL.row > coords.row)) {
          coords = {row: coords.row, col: corners.BR.col};
        }
        else if (priv.settings.fillHandle !== 'vertical') {
          coords = {row: corners.BR.row, col: coords.col};
        }
        else {
          return; //wrong direction
        }
        priv.fillBorder.appear([priv.selStart, priv.selEnd, coords]);
      }
    };

    editproxy = {
      /**
       * Create input field
       */
      init: function () {
        priv.editProxy = $('<textarea class="handsontableInput" >');
        priv.editProxy.on('propertychange',function(){
        	this.style.height = this.scrollHeight + 'px';
        });
        priv.editProxy.on('input',function(){
        	this.style.height = this.scrollHeight + 'px';
        });
        priv.editProxyHolder = $('<div class="handsontableInputHolder">');
        priv.editProxyHolder.append(priv.editProxy);

        function onClick(event) {
          event.stopPropagation();
        }

        function onCut() {
            var arr = grid.getCellsAtCoords(priv.selStart, priv.selEnd);
        	  var str="";
        	  var rowCount=grid.getCellCoords(arr[0]).row;
        	  for(var i=0;i<arr.length;i++){
        		if(!$(arr[i]).is(":hidden")){
        			var coords = grid.getCellCoords(arr[i]);
        			if(rowCount!=coords.row){
        				str +="#";
        				rowCount=coords.row;
        			}else{
        				str +=",";
        			}
        			str +=$(arr[i]).html();
        			$(arr[i]).html("");
        		}
        	  }
        	  str = str.substring(1,str.length);
        	  $("#dp",parent.document).val(str);
        	  
//            autofill.hideHandle();
            return str;
            }
        function onCopy() {
        	
          var arr = grid.getCellsAtCoords(priv.selStart, priv.selEnd);
      	  var str="";
      	  var rowCount=grid.getCellCoords(arr[0]).row;
      	  for(var i=0;i<arr.length;i++){
      		if(!$(arr[i]).is(":hidden")){
      			var coords = grid.getCellCoords(arr[i]);
      			if(rowCount!=coords.row){
      				str +="#";
      				rowCount=coords.row;
      			}else{
      				str +=",";
      			}
      			str +=$(arr[i]).html();
      		}
      	  }
      	  str = str.substring(1,str.length);
      	  $("#dp",parent.document).val(str);
//          autofill.hideHandle();
          return str;
        }
     
        function onPaste() {
        	var pd = parent.document.getElementById("dp").value;
        	var arrR = pd.split('#');
        	for(var i=0;i<arrR.length;i++){
        		var arrC = arrR[i].split(',');
        		for(var j=0;j<arrC.length;j++){
        			var td = grid.getCellAtCoords({row:parseInt(priv.selStart.row+i),col:parseInt(priv.selStart.col+j)});
        			$(td).html(arrC[j]);
        		}
        	}
        	
        	
//          if (!priv.isCellEdited) {
//            setTimeout(function () {
//              var input = priv.editProxy.val().replace(/^[\r\n]*/g, '').replace(/[\r\n]*$/g, ''), //remove newline from the start and the end of the input
//              inputArray = CSVToArray(input, '\t'),
//                  coords = grid.getCornerCoords([priv.selStart, priv.selEnd]),
//                  endTd = grid.populateFromArray(coords.TL, inputArray, {
//                    row: Math.max(coords.BR.row, inputArray.length - 1 + coords.TL.row),
//                    col: Math.max(coords.BR.col, inputArray[0].length - 1 + coords.TL.col)
//                  }, null, 'paste');
//              selection.setRangeEnd(endTd);
//            }, 100);
//          }
        }

        function onKeyDown(event) {
          var r, c;
          priv.lastKeyCode = event.keyCode;
          var ctrlDown = (event.ctrlKey || event.metaKey) && !event.altKey; //catch CTRL but not right ALT (which in some systems triggers ALT+CTRL)
          	if (ctrlDown && event.keyCode === 83) { //CTRL + S
          		Handsontable.XML.saveXML();
          		return false;
              }
          if (selection.isSelected()) {
            if (Handsontable.helper.isPrintableChar(event.keyCode)) {
              if (!priv.isCellEdited && !ctrlDown) { //disregard CTRL-key shortcuts
                editproxy.beginEditing();
              }
              else if (ctrlDown) {
            	if (!priv.isCellEdited && event.keyCode === 83) { //CTRL + S
            		Handsontable.XML.saveXML();
            		return false;
                }else if (!priv.isCellEdited && event.keyCode === 65) { //CTRL + A
                  selection.selectAll(); //select all cells
                }
                else if (!priv.isCellEdited && event.keyCode === 88 ) { //CTRL + X
//                  priv.editProxy.triggerHandler('cut'); //simulate oncut for Opera
                	if (!priv.isCellEdited) {
                        setTimeout(function () {
                          selection.empty();
                        }, 100);
                      }
                }
                else if (!priv.isCellEdited && event.keyCode === 86) { //CTRL + V
//                  priv.editProxy.triggerHandler('paste'); //simulate onpaste for Opera
                	 if (!priv.isCellEdited) {
                         setTimeout(function () {
                           var input = priv.editProxy.val().replace(/^[\r\n]*/g, '').replace(/[\r\n]*$/g, ''), //remove newline from the start and the end of the input
                               inputArray = CSVToArray(input, '\t'),
                               coords = grid.getCornerCoords([priv.selStart, priv.selEnd]),
                               endTd = grid.populateFromArray(coords.TL, inputArray, {
                                 row: Math.max(coords.BR.row, inputArray.length - 1 + coords.TL.row),
                                 col: Math.max(coords.BR.col, inputArray[0].length - 1 + coords.TL.col)
                               }, null, 'paste');
                           selection.setRangeEnd(endTd);
                         }, 100);
                       }
                }
                else if (event.keyCode === 89 || (event.shiftKey && event.keyCode === 90)) { //CTRL + Y or CTRL + SHIFT + Z
                  priv.undoRedo && priv.undoRedo.redo();
                }
                else if (event.keyCode === 90) { //CTRL + Z
                  priv.undoRedo && priv.undoRedo.undo();
                }
              }
              return;
            }
            
            var rangeModifier = event.shiftKey ? selection.setRangeEnd : selection.setRangeStart;

            switch (event.keyCode) {
              case 38: /* arrow up */
                if (isAutoComplete()) {
                	return true;
                }
                if (!priv.isCellEdited) {
                  if (event.shiftKey) {
                    selection.transformEnd(-1, 0);
                  }
                  else {
                    selection.transformStart(-1, 0);
                  }
                  event.preventDefault();
                }
                else {
                  editproxy.finishEditing(false, -1, 0);
                }
                break;

              case 9: /* tab */
                r = priv.settings.tabMoves.row;
                c = priv.settings.tabMoves.col;
                if (!isAutoComplete()) {
                  if (event.shiftKey) {
                    editproxy.finishEditing(false, -r, -c);
                  }
                  else {
                    editproxy.finishEditing(false, r, c);
                  }
                }
                event.preventDefault();
                break;

              case 39: /* arrow right */
                if (!priv.isCellEdited) {
                  if (event.shiftKey) {
                    selection.transformEnd(0, 1);
                  }
                  else {
                    selection.transformStart(0, 1);
                  }
                  event.preventDefault();
                }
                else if (editproxy.getCaretPosition() === priv.editProxy.val().length) {
                  editproxy.finishEditing(false, 0, 1);
                  if (isAutoComplete() && isAutoComplete().shown) {
                    isAutoComplete().hide();
                  }
                }
                break;
              case 37: /* arrow left */
                if (!priv.isCellEdited) {
                  if (event.shiftKey) {
                    selection.transformEnd(0, -1);
                  }
                  else {
                    selection.transformStart(0, -1);
                  }
                  event.preventDefault();
                }
                else if (editproxy.getCaretPosition() === 0) {
                  editproxy.finishEditing(false, 0, -1);
                  if (isAutoComplete() && isAutoComplete().shown) {
                    isAutoComplete().hide();
                  }
                }
                break;

              case 8: /* backspace */
              case 46: /* delete */
                if (!priv.isCellEdited) {
                  selection.empty(event);
                  event.preventDefault();
                  
                  var $td = $(grid.getCellAtCoords(priv.selStart));
                  var format=$td.attr('format')==undefined?$td.prop('format'):$td.attr('format');
                  if(format&&priv.editProxy.val()==""){
                	  $td.attr('format',null);
                  }
                  //TODO 重新解析table 存入cookie
            		var XMLData = Handsontable.XML.parsingToXML();
            		$tableModel.val(XMLData);
            		parent.rtree.initModelTree();
                }
                break;

              case 40: /* arrow down */
                if (!priv.isCellEdited) {
                  if (event.shiftKey) {
                    selection.transformEnd(1, 0); 	
                  }
                  else {
                    selection.transformStart(1, 0); //move selection down
                  }
                }
                else {
                  if (isAutoComplete()) { //if browsing through autocomplete
                    return true;
                  }
                  else {
                    editproxy.finishEditing(false, 1, 0);
                  }
                }
                break;

              case 27: /* ESC */
                if (priv.isCellEdited) {
                  editproxy.finishEditing(true, 0, 0); //hide edit field, restore old value, don't move selection, but refresh routines
                }
                break;

              case 113: /* F2 */
                if (!priv.isCellEdited) {
                  editproxy.beginEditing(true); //show edit field
                  event.preventDefault(); //prevent Opera from opening Go to Page dialog
                }
                break;

              case 13: /* return/enter */
                r = priv.settings.enterMoves.row;
                c = priv.settings.enterMoves.col;
                if (priv.isCellEdited) {
                  if ((event.ctrlKey && !selection.isMultiple()) || event.altKey) { //if ctrl+enter or alt+enter, add new line
                    priv.editProxy.val(priv.editProxy.val() + '\n');
                    priv.editProxy[0].focus();
                  }
                  else if (!isAutoComplete()) {
                    if (event.shiftKey) { //if shift+enter, finish and move up
                      editproxy.finishEditing(false, -r, -c, ctrlDown);
                    }
                    else { //if enter, finish and move down
                      editproxy.finishEditing(false, r, c, ctrlDown);
                    }
                  }
                }
                else {
                  if (event.shiftKey) {
                    selection.transformStart(-r, -c); //move selection up
                  }
                  else {
                    if (priv.settings.enterBeginsEditing) {
                      if ((ctrlDown && !selection.isMultiple()) || event.altKey) { //if ctrl+enter or alt+enter, add new line
                        editproxy.beginEditing(true, '\n'); //show edit field
                      }
                      else {
                        editproxy.beginEditing(true); //show edit field
                      }
                    }
                    else {
                      selection.transformStart(r, c, true); //move selection down or create new row
                    }
                  }
                }
                event.preventDefault(); //don't add newline to field
                break;

              case 36: /* home */
                if (!priv.isCellEdited) {
                  if (event.ctrlKey || event.metaKey) {
                    rangeModifier(grid.getCellAtCoords({row: 0, col: priv.selStart.col}));
                  }
                  else {
                    rangeModifier(grid.getCellAtCoords({row: priv.selStart.row, col: 0}));
                  }
                }
                break;

              case 35: /* end */
                if (!priv.isCellEdited) {
                  if (event.ctrlKey || event.metaKey) {
                    rangeModifier(grid.getCellAtCoords({row: self.rowCount - 1, col: priv.selStart.col}));
                  }
                  else {
                    rangeModifier(grid.getCellAtCoords({row: priv.selStart.row, col: self.colCount - 1}));
                  }
                }
                break;

              case 33: /* pg up */
                rangeModifier(grid.getCellAtCoords({row: 0, col: priv.selStart.col}));
                break;

              case 34: /* pg dn */
                rangeModifier(grid.getCellAtCoords({row: self.rowCount - 1, col: priv.selStart.col}));
                break;

              default:
                break;
            }
          }
        }

        function onChange() {
          var move;
          if (isAutoComplete()) { //could this change be from autocomplete
            var val = priv.editProxy.val();
            if (val !== lastChange && val === priv.lastAutoComplete) { //is it change from source (don't trigger on partial)
              priv.isCellEdited = true;
              if (priv.lastKeyCode === 9) { //tab
                move = priv.settings.tabMoves;
              }
              else { //return/enter
                move = priv.settings.enterMoves;
              }
              editproxy.finishEditing(false, move.row, move.col);
            }
            lastChange = val;
          }
        }
        priv.editProxy.on('click', onClick);
        priv.editProxy.on('cut', onCut);
        priv.editProxy.on('copy', onCopy);
        priv.editProxy.on('paste', onPaste);
        priv.editProxy.on('keydown', onKeyDown);
        $(parent.document.body).bind('keydown', function(event){
        	 var ctrlDown = (event.ctrlKey || event.metaKey) && !event.altKey; //catch CTRL but not right ALT (which in some systems triggers ALT+CTRL)
           	if (ctrlDown && event.keyCode === 83) { //CTRL + S
           		Handsontable.XML.saveXML();
           		return false;
               }
        });
        priv.editProxy.on('change', onChange);
        container.append(priv.editProxyHolder);
      },

      /**
       * Prepare text input to be displayed at given grid cell
       */
      prepare: function () {
        if (priv.isCellEdited) {
          return;
        }

        priv.editProxy.height(priv.editProxy.parent().innerHeight() - 4);
        priv.editProxy.val(datamap.getText(priv.selStart, priv.selEnd));
        setTimeout(editproxy.focus, 1);

        if (priv.settings.autoComplete) {
          var typeahead = priv.editProxy.data('typeahead');
          if (!typeahead) {
            priv.editProxy.typeahead({
              updater: function (item) {
                priv.lastAutoComplete = item;
                return item
              }
            });
            typeahead = priv.editProxy.data('typeahead');
          }
          typeahead.source = [];
          for (var i = 0, ilen = priv.settings.autoComplete.length; i < ilen; i++) {
            if (priv.settings.autoComplete[i].match(priv.selStart.row, priv.selStart.col, datamap.getAll)) {
              typeahead.source = priv.settings.autoComplete[i].source(priv.selStart.row, priv.selStart.col);
              typeahead.highlighter = priv.settings.autoComplete[i].highlighter || defaultAutoCompleteHighlighter;
              break;
            }
          }
        }

        var current = grid.getCellAtCoords(priv.selStart);
        var $current = $(current);
        var currentOffset = $current.offset();
        var containerOffset = container.offset();
        var scrollTop = container.scrollTop();
        var scrollLeft = container.scrollLeft();
        var editTop = currentOffset.top - containerOffset.top + scrollTop - 1;
        var editLeft = currentOffset.left - containerOffset.left + scrollLeft - 1;
        
        
        
        if (editTop < 0) {
          editTop = 0;
        }
        if (editLeft < 0) {
          editLeft = 0;
        }
        if (self.blockedRows.count() > 0 && parseInt($current.css('border-top-width')) > 0) {
          editTop += 1;
        }
        if (self.blockedCols.count() > 0 && parseInt($current.css('border-left-width')) > 0) {
          editLeft += 1;
        }
        
        if ($.browser.msie && parseInt($.browser.version, 10) <= 7) {
          editTop -= 1;
        }

        priv.editProxyHolder.addClass('htHidden');
        priv.editProxyHolder.css({
          top: editTop,
          left: editLeft,
          overflow: 'hidden'
        });
        priv.editProxy.css({
          width: 0,
          height: 0
        });
      },

      /**
       * Sets focus to textarea
       */
      focus: function () {
        priv.editProxy[0].select();
      },

      /**
       * Returns caret position in edit proxy
       * @author http://stackoverflow.com/questions/263743/how-to-get-caret-position-in-textarea
       * @return {Number}
       */
      getCaretPosition: function () {
        var el = priv.editProxy[0];
        if (el.selectionStart) {
          return el.selectionStart;
        }
        else if (document.selection) {
          el.focus();
          var r = document.selection.createRange();
          if (r == null) {
            return 0;
          }
          var re = el.createTextRange(),
              rc = re.duplicate();
          re.moveToBookmark(r.getBookmark());
          rc.setEndPoint('EndToStart', re);
          return rc.text.length;
        }
        return 0;
      },

      /**
       * Sets caret position in edit proxy
       * @author http://blog.vishalon.net/index.php/javascript-getting-and-setting-caret-position-in-textarea/
       * @param {Number}
          */
      setCaretPosition: function (pos) {
        var el = priv.editProxy[0];
        if (el.setSelectionRange) {
          el.focus();
          el.setSelectionRange(pos, pos);
        }
        else if (el.createTextRange) {
          var range = el.createTextRange();
          range.collapse(true);
          range.moveEnd('character', pos);
          range.moveStart('character', pos);
          range.select();
        }
      },

      /**
       * Shows text input in grid cell
       * @param {Boolean} useOriginalValue
       * @param {String} suffix
       */
      beginEditing: function (useOriginalValue, suffix) {
        if (priv.isCellEdited) {
          return;
        }
        selection.deselect();
        var td = grid.getCellAtCoords(priv.selStart),
            $td = $(td);

        if (!grid.isCellWritable($td)) {
          return;
        }

        if (priv.fillHandle) {
          autofill.hideHandle();
        }

        priv.isCellEdited = true;
        lastChange = '';
        if (useOriginalValue) {
          var original = datamap.get(priv.selStart.row, priv.selStart.col) + (suffix || '');
          priv.editProxy.val(original);
          editproxy.setCaretPosition(original.length);
        }
        else {
          priv.editProxy.val('');
        }

        var width, height;
        if (priv.editProxy.autoResize) {
          width = $td.width();
          height = $td.outerHeight() - 4;
        }
        else {
          width = $td.width();
          height = $td.height();
        }

        if (parseInt($td.css('border-top-width')) > 0) {
          height -= 1;
        }
        if (parseInt($td.css('border-left-width')) > 0) {
          if (self.blockedCols.count() > 0) {
            width -= 1;
          }
        }

        if (priv.editProxy.autoResize) {
          priv.editProxy.autoResize({
            maxHeight: 200,
            minHeight: height,
            minWidth: width,
            maxWidth: Math.max(168, width),
            animate: false,
            extraSpace: 0
          });
        }
        else {
          priv.editProxy.css({
            width: width,
            height: height
          });
        }
        priv.editProxyHolder.removeClass('htHidden');
        priv.editProxyHolder.css({
          overflow: 'visible'
        });
      },

      /**
       * Finishes text input in selected cells
       * @param {Boolean} [isCancelled] If TRUE, restore old value instead of using current from editproxy
       * @param {Number} [moveRow] Move selection row if edit is not cancelled
       * @param {Number} [moveCol] Move selection column if edit is not cancelled
       * @param {Boolean} [ctrlDown] If true, apply to all selected cells
       */
      finishEditing: function (isCancelled, moveRow, moveCol, ctrlDown) {
        if (priv.isCellEdited) {
          priv.isCellEdited = false;
          var val = [
//            [$.trim(priv.editProxy.val())]
			[priv.editProxy.val()]
          ];
          if (!isCancelled) {
            var endTd;
            if (ctrlDown) { //if ctrl+enter and multiple cells selected, behave like Excel (finish editing and apply to all cells)
              var corners = grid.getCornerCoords([priv.selStart, priv.selEnd]);
              endTd = grid.populateFromArray(corners.TL, val, corners.BR, false, 'edit');
            }
            else {
              endTd = grid.populateFromArray(priv.selStart, val, null, false, 'edit');
            }
          }
          
          var $td = $(grid.getCellAtCoords(priv.selStart));
          ///////////////////////////////////////////
          var format=$td.attr('format')==undefined?$td.prop('format'):$td.attr('format');
          if(format&&priv.editProxy.val()==""){
        	  $td.attr('format',null);
          }
          ///////////////////////////////////////////
          var expData=$td.attr('expData')==undefined?$td.prop('expData'):$td.attr('expData');
          if(expData){
        	  var arrs = expData.split('#');
        	  for(var j =0;j<arrs.length;j++){
        		  var arr = arrs[j].split(',');
        		  if(arr[0]=="sum"){
            		  var sum = 0;
            		  var arrData = sumData[arr[1]+","+arr[2]];
        			  for(var i = 0;i<arrData.length;i++){
        				  var tmp = parseInt($(arrData[i]).html());
        				  if(!isNaN(tmp)){
      						sum +=tmp;
      					}
        			  }
        			 var $td =  $(grid.getCellAtCoords({row:parseInt(arr[1]),col:parseInt(arr[2])}));
        			 $td.html(sum);
        			 $td.append('<div class="exp_flag"></div>');
            	  }else if (arr[0]=="average"){
            		  var average = 0;
            		  var count=0;
            		  var arrData = averageData[arr[1]+","+arr[2]];
        			  for(var i = 0;i<arrData.length;i++){
        				  var tmp = parseInt($(arrData[i]).html());
        				  if(!isNaN(tmp)){
        					  average +=tmp;
        					  count++;
      					}
        			  }
        			  average = average/count;
        			  var $td =  $(grid.getCellAtCoords({row:parseInt(arr[1]),col:parseInt(arr[2])}));
        			  $td.html(average);
        			  $td.append('<div class="exp_flag"></div>');
            	  }else if (arr[0]=="count"){
            		  var count=0;
            		  var arrData = countData[arr[1]+","+arr[2]];
        			  for(var i = 0;i<arrData.length;i++){
        				  if($(arrData[i]).html()!=""){
        					  count++;
      					}
        			  }
        			  var $td =  $(grid.getCellAtCoords({row:parseInt(arr[1]),col:parseInt(arr[2])}));
        			  $td.html(count);
        			  $td.append('<div class="exp_flag"></div>');
            	  }else if (arr[0]=="discount"){
            		  var discount=0;
            		  var arrData =discountData[arr[1]+","+arr[2]];
        			  for(var i = 0;i<arrData.length;i++){
        				  if($(arrData[i]).html()!=""){
        					  discount++;
      					}
        			  }
        			  var $td =  $(grid.getCellAtCoords({row:parseInt(arr[1]),col:parseInt(arr[2])}));
        			  $td.html(discount);
        			  $td.append('<div class="exp_flag"></div>');
            	  }else if (arr[0]=="max"){
            		  var max = 0;
            		  var arrData = maxData[arr[1]+","+arr[2]];
        			  for(var i = 0;i<arrData.length;i++){
        				  var tmp = parseInt($(arrData[i]).html());
        				  if(!isNaN(tmp)){
        					  if(tmp>max){
        						  max=tmp;
        					  }
      					}
        			  }
        			  var $td =  $(grid.getCellAtCoords({row:parseInt(arr[1]),col:parseInt(arr[2])}));
        			  $td.html(max);
        			  $td.append('<div class="exp_flag"></div>');
            	  }else if (arr[0]=="min"){
            		  var min = 0;
            		  var arrData =minData[arr[1]+","+arr[2]];
        			  for(var i = 0;i<arrData.length;i++){
        				  var tmp = parseInt($(arrData[i]).html());
        				  if(!isNaN(tmp)){
        					  if(i==0){
        						  min = tmp; 
        					  }else if(tmp<min){
        						  min=tmp;
        					  }
      					}
        			  }
        			  var $td =  $(grid.getCellAtCoords({row:parseInt(arr[1]),col:parseInt(arr[2])}));
        			  $td.html(min);
        			  $td.append('<div class="exp_flag"></div>');
            	  }
        	  }
          }
          //TODO 重新解析table 存入cookie
      		var XMLData = Handsontable.XML.parsingToXML();
      	    parent.rtree.updateNode(priv.selStart.row,priv.selStart.col,val);
      		$tableModel.val(XMLData);
      		parent.rtree.initModelTree();
          
          priv.editProxy.css({
            width: 0,
            height: 0
          });
          priv.editProxyHolder.addClass('htHidden');
          priv.editProxyHolder.css({
            overflow: 'hidden'
          });
        }
        if (endTd && typeof moveRow !== "undefined" && typeof moveCol !== "undefined") {
          selection.transformStart(moveRow, moveCol, !priv.settings.enterBeginsEditing);
        }
      }
    };

    interaction = {
      onMouseDown: function (event) {
        priv.isMouseDown = true;
        if (event.button === 2 && selection.inInSelection(grid.getCellCoords(this))) { //right mouse button
          //do nothing
        }
        else if (event.shiftKey) {
        	//alert($(this).html())
          selection.setRangeEnd(this);
        }
        else {
          if(document.getElementById('ExpFunDiv')==null){
        	  selection.deselect();
    		  var coords = grid.getCellCoords(this);
    		  startCellCoord = coords;
    		  priv.currentBorder.appear([coords]);
    		  priv.selStart = coords;
    		  cellCoords.cellStart = coords;
    		  priv.selEnd = coords;
       		  cellCoords.cellEnd = coords;
    		  priv.fillHandle.appear([{row:coords.row,col:coords.col}, {row:coords.row,col:coords.col}]);
    		  priv.selectionBorder.appear([{row:coords.row,col:coords.col}, {row:coords.row,col:coords.col}]);
    		  editproxy.prepare();
    		  parent.rprop.SynProperties('',null);
    		  parent.rtree.selectByParam(coords.row,coords.col);
    		  self.selectStyle();
    		  
    		  for(var k in sumData){
      			 for(var z=0;z<sumData[k].length;z++){
      				 $($(sumData[k][z]).find('div')[0]).remove();
      			 }
      		  }
    		  for(var k in averageData){
 		  			for(var z=0;z<averageData[k].length;z++){
 		  				$($(averageData[k][z]).find('div')[0]).remove();
 		  			}
 		  	  }
    		  for(var k in countData){
       			 for(var z=0;z<countData[k].length;z++){
       				 $($(countData[k][z]).find('div')[0]).remove();
       			 }
       		  }
    		  for(var k in discountData){
        			 for(var z=0;z<discountData[k].length;z++){
        				 $($(discountData[k][z]).find('div')[0]).remove();
        			 }
        		  }
    		  for(var k in maxData){
        			 for(var z=0;z<maxData[k].length;z++){
        				 $($(maxData[k][z]).find('div')[0]).remove();
        			 }
        		  }
    		  for(var k in minData){
        			 for(var z=0;z<minData[k].length;z++){
        				 $($(minData[k][z]).find('div')[0]).remove();
        			 }
        		  }
    		  var expType=$(this).attr('expType')==undefined?$(this).prop('expType'):$(this).attr('expType');
    		  var arr = new Array();
    		  if(expType=="sum"){
    			  arr = sumData[coords.row+","+coords.col];
    			  for(var i = 0;i<arr.length;i++){
    				  $(arr[i]).append('<div class="exp_sum"></div>');
    			  }
    		  }else if(expType=="average"){
    			  arr = averageData[coords.row+","+coords.col];
    			  for(var i = 0;i<arr.length;i++){
    				  $(arr[i]).append('<div class="exp_average"></div>');
    			  }
    		  }else if(expType=="count"){
    			  arr = countData[coords.row+","+coords.col];
    			  for(var i = 0;i<arr.length;i++){
    				  $(arr[i]).append('<div class="exp_count"></div>');
    			  }
    		  }else if(expType=="discount"){
    			  arr = discountData[coords.row+","+coords.col];
    			  for(var i = 0;i<arr.length;i++){
    				  $(arr[i]).append('<div class="exp_discount"></div>');
    			  }
    		  }else if(expType=="max"){
    			  arr = maxData[coords.row+","+coords.col];
    			  for(var i = 0;i<arr.length;i++){
    				  $(arr[i]).append('<div class="exp_max"></div>');
    			  }
    		  }else if(expType=="min"){
    			  arr = minData[coords.row+","+coords.col];
    			  for(var i = 0;i<arr.length;i++){
    				  $(arr[i]).append('<div class="exp_min"></div>');
    			  }
    		  }
    		  
          }else{
        	  var coords = grid.getCellCoords(this);
        	  var lengths=0;
        	  if($('#ExpFunDiv').html().indexOf("SUM") > 0 ){	
        		  if($(this).children().length>0){
        			  return;
        		  }
        		  $(this).append('<div class="exp_sum"></div>');
        	      //TODO
        	      expData.sum[expData.sum.length] = this;
        	      lengths =expData.sum.length;
        	  }else if($('#ExpFunDiv').html().indexOf("AVERAGE") > 0 ){
        		  if($(this).children().length>0){
        			  return;
        		  }
        		  $(this).append('<div class="exp_average"></div>');
        		  expData.average[expData.average.length] = this;
        		  lengths =expData.average.length;
        	  }else if($('#ExpFunDiv').html().indexOf("COUNT") > 0 ){
        		  if($(this).children().length>0){
        			  return;
        		  }
        		  $(this).append('<div class="exp_count"></div>');
        		  expData.count[expData.count.length] = this;
        		  lengths =expData.count.length;
        	  }else if($('#ExpFunDiv').html().indexOf("DISCOUNT") > 0 ){
        		  if($(this).children().length>0){
        			  return;
        		  }
        		  $(this).append('<div class="exp_discount"></div>');
        		  expData.discount[expData.discount.length] = this;
        		  lengths =expData.discount.length;
        	  }else if($('#ExpFunDiv').html().indexOf("MAX") > 0 ){
        		  if($(this).children().length>0){
        			  return;
        		  }
        		  $(this).append('<div class="exp_max"></div>');
        		  expData.max[expData.max.length] = this;
        		  lengths =expData.max.length;
        	  }else if($('#ExpFunDiv').html().indexOf("MIN") > 0 ){
        		  if($(this).children().length>0){
        			  return;
        		  }
        		  $(this).append('<div class="exp_min"></div>');
        		  expData.min[expData.min.length] = this;
        		  lengths =expData.min.length;
        	  }
        	  if(coords.row+1<27){
    	    	  if(lengths==0){
    	    		  $('#ExpFunDiv').html($('#ExpFunDiv').html()+String.fromCharCode(64 + parseInt(coords.col+1))+parseInt(coords.row+1)); 
    	    	  }else{
    	    		  $('#ExpFunDiv').html($('#ExpFunDiv').html()+","+String.fromCharCode(64 + parseInt(coords.col+1))+parseInt(coords.row+1)); 
    	    	  }
    	      }else{
    	    	  
    	      }
          }
        }
      },

      onMouseOver: function () {
        if (priv.isMouseDown) {
        	if(document.getElementById('ExpFunDiv')==null){
        		var coords = grid.getCellCoords(this);
     			priv.selEnd = coords;
       			cellCoords.cellEnd = coords;
       			coords = highlight.onCellBorder();
       			priv.selectionBorder.appear([{row:coords.start.row,col:coords.start.col}, {row:coords.end.row,col:coords.end.col}]);
    			priv.fillHandle.appear([{row:coords.start.row,col:coords.start.col}, {row:coords.end.row,col:coords.end.col}]);
    			self.selectStyle();
        	}else{
        		/*var coords = grid.getCellCoords(this);
     			priv.selEnd = coords;
        		$(this).css('background-color','#CFDBF3');
        		if(coords.row+1<27){
      	    	  $('#ExpFunDiv').html($('#ExpFunDiv').html()+","+String.fromCharCode(64 + parseInt(coords.col+1))+parseInt(coords.row+1)); 
      	      	}else{
      	    	  
      	      	}
      	      	expData.sum[expData.sum.length] = this;*/
        	}
 			
        }
        else if (priv.fillHandle && priv.fillHandle.isDragged) {
          priv.fillHandle.isDragged++;
          autofill.showBorder(this);
        }
      },

      onDblClick: function () {
    	var td = grid.getCellAtCoords({row: priv.selStart.row,col: priv.selStart.col});
    	if($(td).html().indexOf("=")==0) return;
    	//禁止编辑
    	var expType=$(td).attr('expType')==undefined?$(td).prop('expType'):$(td).attr('expType');
    	if((expType!=undefined&&expType!="")||$(td).find('a').length>0){
    		return;
    	}
        priv.editProxy[0].focus();
        editproxy.beginEditing(true);
        if (priv.settings.autoComplete) {
          priv.editProxy.data('typeahead').lookup();
        }
      }
    };

    this.init = function () {
      function onMouseEnterTable() {
        priv.isMouseOverTable = true;
      }

      function onMouseLeaveTable() {
        priv.isMouseOverTable = false;
      }

      self.curScrollTop = self.curScrollLeft = 0;
      self.lastScrollTop = self.lastScrollLeft = null;
      priv.scrollbarSize = measureScrollbar();

      var div = $('<div id="dataTable"><table cellspacing="0" cellpadding="0"><thead></thead><tbody></tbody></table></div>');
      priv.tableContainer = div[0];
      self.table = $(priv.tableContainer.firstChild);
      priv.tableBody = self.table.find("tbody")[0];
      self.table.on('mousedown', 'td', interaction.onMouseDown);
      self.table.on('mouseover', 'td', interaction.onMouseOver);
      self.table.on('dblclick', 'td', interaction.onDblClick);
      container.append(div);

      self.colCount = settings.cols;
      self.rowCount = 0;

      highlight.init();
      priv.currentBorder = new Border(container, {
        className: 'current',
        bg: true
      });
      editproxy.init();

      this.updateSettings(settings);
//	  thdrafting.init();
      container.on('mouseenter', onMouseEnterTable).on('mouseleave', onMouseLeaveTable);
      $(priv.currentBorder.main).on('dblclick', interaction.onDblClick);

      function onMouseUp() {
      	isThHander = false;
        if (priv.isMouseDown) {
          setTimeout(editproxy.focus, 1);
        }
        priv.isMouseDown = false;
        if (priv.fillHandle && priv.fillHandle.isDragged) {
          if (priv.fillHandle.isDragged > 1) {
            autofill.apply();
          }
          priv.fillHandle.isDragged = 0;
        }
        self.selectedCoords= self.getSelected();
      }

      function onOutsideClick(event) {
        setTimeout(function () {//do async so all mouseenter, mouseleave events will fire before
          if (!priv.isMouseOverTable && event.target !== priv.tableContainer && $(event.target).attr('id') !== 'context-menu-layer') { //if clicked outside the table or directly at container which also means outside
//          selection.deselect();
          }
        }, 1);
      }

      $("html").on('mouseup', onMouseUp);
      $("html").on('click', onOutsideClick);

      if (container[0].tagName.toLowerCase() !== "html" && container[0].tagName.toLowerCase() !== "body" && (container.css('overflow') === 'scroll' || container.css('overflow') === 'auto')) {
        priv.scrollable = container;
      }
      else {
        container.parents().each(function () {
          if (this.tagName.toLowerCase() !== "html" && this.tagName.toLowerCase() !== "body" && ($(this).css('overflow') === 'scroll' || $(this).css('overflow') === 'auto')) {
            priv.scrollable = $(this);
            return false;
          }
        });
      }

      if (priv.scrollable) {
        priv.scrollable.scrollTop(0);
        priv.scrollable.scrollLeft(0);

        priv.scrollable.on('scroll.handsontable', function () {
          self.curScrollTop = priv.scrollable[0].scrollTop;
          self.curScrollLeft = priv.scrollable[0].scrollLeft;

          if (self.curScrollTop !== self.lastScrollTop) {
            self.blockedRows.refreshBorders();
            self.blockedRows.main[0].style.top = self.curScrollTop + 'px';
          }

          if (self.curScrollLeft !== self.lastScrollLeft) {
            self.blockedCols.refreshBorders();
            self.blockedCols.main[0].style.left = self.curScrollLeft + 'px';
          }

          if (priv.cornerHeader && (self.curScrollTop !== self.lastScrollTop || self.curScrollLeft !== self.lastScrollLeft)) {
            if (self.curScrollTop === 0 && self.curScrollLeft === 0) {
              priv.cornerHeader.find("th:last-child").css({borderRightWidth: 0});
              priv.cornerHeader.find("tr:last-child th").css({borderBottomWidth: 0});
            }
            else if (self.lastScrollTop === 0 && self.lastScrollLeft === 0) {
              priv.cornerHeader.find("th:last-child").css({borderRightWidth: '1px'});
              priv.cornerHeader.find("tr:last-child th").css({borderBottomWidth: '1px'});
            }
            priv.cornerHeader[0].style.top = self.curScrollTop + 'px';
            priv.cornerHeader[0].style.left = self.curScrollLeft + 'px';
          }

          self.lastScrollTop = self.curScrollTop;
          self.lastScrollLeft = self.curScrollLeft;
        });
        priv.scrollable.trigger('scroll.handsontable');
      }
      else {
        priv.scrollable = $(window);
        if (priv.cornerHeader) {
          priv.cornerHeader.find("th:last-child").css({borderRightWidth: 0});
          priv.cornerHeader.find("tr:last-child th").css({borderBottomWidth: 0});
        }
      }

      priv.scrollable.on('scroll', function (e) {
        e.stopPropagation();
      });

      if (priv.settings.contextMenu) {
        var onContextClick = function (key) {
          var coords = grid.getCornerCoords([priv.selStart, priv.selEnd]);
      	var $td = $(grid.getCellAtCoords(coords.TL));

          switch (key) {
          	case "rowHidde":
//          		self.rowHidde();
//          		selection.deselect();
              break;
          	case "colHidde":
//          		self.colHidde();
//          		selection.deselect();
              break;
        	case "rowShow":
//        		self.rowShow();
//        		selection.deselect();
                break;
            case "colShow":
//            	self.colShow();
//            	selection.deselect();
                break;
            case "linkOpt":
            	parent.showdialog('配置链接','Data_Link.jsp');
            	break;
            case "linkclear":
            	Handsontable.expression.clearHyperlink();
            	break;
            case "frameOpt":
            	var frameid=$td.attr('frameid')==undefined?"":$td.attr('frameid');
            	parent.showdialog('配置子报表','Data_Report.jsp?frameid='+frameid);
            	break;
            case "frameclear":
            	Handsontable.expression.clearFrameid();
            	break;
          	case "Mergecells":
              self.Mergecells();
              selection.deselect();
              break;
          	case "MergecellsOpen":
              self.MergecellsOpen();
              selection.deselect();
              break;
            case "row_above":
              //grid.alter("insert_row", coords.TL);
            	self.row_above();
              break;

            case "row_below":
              //isTHMenu = true;
              //grid.alter("insert_row", {row: coords.BR.row + 1, col: 0});
              self.row_below();
              break;

            case "col_left":
              //grid.alter("insert_col", coords.TL);
            	self.col_left();
              break;

            case "col_right":
              //isTHMenu = true;
              //grid.alter("insert_col", {row: 0, col: coords.BR.col + 1});
              self.col_right();
              break;

            case "remove_row":
            case "remove_col":
              //grid.alter(key, coords.TL, coords.BR);
            	self.remove(key);
              break;

            case "undo":
            case "redo":
              priv.undoRedo[key]();
              break;
            case "fillreport":
            	parent.showdialog('配置填报属性','Fill_Report.jsp');
            	break;
          }
          //TODO 重新解析table 存入cookie
    		var XMLData = Handsontable.XML.parsingToXML();
    		$tableModel.val(XMLData);
    		parent.rtree.initModelTree();
        };

        var isDisabled = function (key) {
          if (self.blockedCols.main.find('th.htRowHeader.r-active').length && (key === "remove_col" || key === "col_left" || key === "col_right")) {
            return true;
          }

          if (self.blockedRows.main.find('th.htColHeader.c-active').length && (key === "remove_row" || key === "row_above" || key === "row_below")) {
            return true;
          }

          if (priv.selStart) {
            var coords = grid.getCornerCoords([priv.selStart, priv.selEnd]);
            if (((key === "row_above" || key === "remove_row") && coords.TL.row === 0) || ((key === "col_left" || key === "remove_col") && coords.TL.col === 0)) {
              if ($(grid.getCellAtCoords(coords.TL)).data("readOnly")) {
                return true;
              }
            }else if(key === "linkclear"){
            	var $td = $(grid.getCellAtCoords(coords.TL));
            	var link=$td.attr('link')==undefined?$td.prop('link'):$td.attr('link');
            	if (link=="") {
                    return true;
                }
            }
            return false;
          }

          return true;
        };

        var allItems = {
          "undo": {name: "Undo", disabled: function () {
            return priv.undoRedo ? !priv.undoRedo.isUndoAvailable() : true
          }},
          "redo": {name: "Redo", disabled: function () {
            return priv.undoRedo ? !priv.undoRedo.isRedoAvailable() : true
          }},
          "sep1": "---------",
          "row_above": {name: "向上插入行", disabled: isDisabled},
          "row_below": {name: "向下插入行", disabled: isDisabled},
          "sep2": "---------",
          "col_left": {name: "向左插入列", disabled: isDisabled},
          "col_right": {name: "向右插入列", disabled: isDisabled},
          "sep3": "---------",
          "remove_row": {name: "删除行", disabled: isDisabled},
          "remove_col": {name: "删除列", disabled: isDisabled},
          "sep4": "---------",
          "Mergecells": {name: "合并单元格", disabled: isDisabled},
          "MergecellsOpen": {name: "打开单元格", disabled: isDisabled},
         /* "sep5": "---------",
          "rowHidde": {name: "隐藏行", disabled: isDisabled},
          "colHidde": {name: "隐藏列", disabled: isDisabled},
          "sep6": "---------",
          "rowShow": {name: "显示行", disabled: isDisabled},
          "colShow": {name: "显示列", disabled: isDisabled}*/
          "sep5": "---------",
          "linkOpt": {name: "配置链接", disabled: isDisabled},
          "linkclear": {name: "取消链接", disabled: isDisabled},
          "frameOpt": {name: "配置子报表", disabled: isDisabled},
          "frameclear": {name: "取消子报表", disabled: isDisabled},
          "fillreport": {name: "配置填报属性", disabled: isDisabled},
        };

        if (priv.settings.contextMenu === true) { //contextMenu is true, not an array
          priv.settings.contextMenu = ["row_above", "row_below", "sep2", "col_left", "col_right", "sep3", "remove_row", "remove_col","sep4","Mergecells","MergecellsOpen","sep5","linkOpt","linkclear","frameOpt","frameclear","fillreport"]; //use default fields array
        }

        var items = {};
        for (var i = 0, ilen = priv.settings.contextMenu.length; i < ilen; i++) {
          items[priv.settings.contextMenu[i]] = allItems[priv.settings.contextMenu[i]];
        }

        $.contextMenu({
          selector: container.attr('id') ? ("#" + container.attr('id')) : "." + container[0].className.replace(/[\s]+/g, '.'),
          trigger: 'right',
          callback: onContextClick,
          items: items
        });

        $('.context-menu-root').on('mouseenter', onMouseEnterTable).on('mouseleave', onMouseLeaveTable);
      }

      self.container.on("beforedatachange.handsontable", function (event, changes) {
        if (priv.settings.autoComplete) { //validate strict autocompletes
          var typeahead = priv.editProxy.data('typeahead');
          loop : for (var c = 0, clen = changes.length; c < clen; c++) {
            for (var a = 0, alen = priv.settings.autoComplete.length; a < alen; a++) {
              var autoComplete = priv.settings.autoComplete[a];
              var source = autoComplete.source();
              if (autoComplete.match(changes[c][0], changes[c][1], datamap.getAll)) {
                var lowercaseVal = changes[c][3].toLowerCase();
                for (var s = 0, slen = source.length; s < slen; s++) {
                  if (changes[c][3] === source[s]) {
                    continue loop; //perfect match
                  }
                  else if (lowercaseVal === source[s].toLowerCase()) {
                    changes[c][3] = source[s]; //good match, fix the case
                    continue loop;
                  }
                }
                if (autoComplete.strict) {
                  changes[c][3] = false; //no match, invalidate this change
                }
              }
            }
          }
        }

        if (priv.settings.onBeforeChange) {
          var result = priv.settings.onBeforeChange(changes);
          if (result === false) {
            changes.splice(0, changes.length); //invalidate all changes (remove everything from array)
          }
        }
      });
      self.container.on("datachange.handsontable", function (event, changes, source) {
        if (priv.settings.onChange) {
          priv.settings.onChange(changes, source);
        }
      });
    };

    /**
     * Set data at given cell
     * @public
     * @param {Number|Array} row or array of changes in format [[row, col, value, allowHtml], ...]
     * @param {Number} col
     * @param {String} value
     * @param {Boolean} allowHtml
     * @param {String} [source='edit'] String that identifies how this change will be described in changes array (useful in onChange callback)
     */
    this.setDataAtCell = function (row, col, value, allowHtml, source) {
      var refreshRows = false, refreshCols = false, changes, i, ilen;

      if (typeof row === "object") { //is stringish
        changes = row;
      }
      else if (typeof value === "object") { //backwards compatibility
        changes = value;
      }
      else {
        changes = [
          [row, col, value, allowHtml]
        ];
      }

      for (i = 0, ilen = changes.length; i < ilen; i++) {
        changes[i].splice(2, 0, datamap.get(changes[i][0], changes[i][1])); //add old value at index 2
      }

      self.container.triggerHandler("beforedatachange.handsontable", [changes]);

      for (i = 0, ilen = changes.length; i < ilen; i++) {
        if (changes[i][3] === false) {
          continue;
        }

        row = changes[i][0];
        col = changes[i][1];
        value = changes[i][3];
        allowHtml = changes[i][4] || allowHtml;

        if (priv.settings.minSpareRows) {
          while (row > self.rowCount - 1) {
            datamap.createRow();
            grid.createRow();
            refreshRows = true;
          }
        }
        if (priv.settings.minSpareCols) {
          while (col > self.colCount - 1) {
            datamap.createCol();
            grid.createCol();
            refreshCols = true;
          }
        }

        var td = grid.getCellAtCoords({row: row, col: col});
        switch (typeof value) {
          case 'string':
            break;

          case 'number':
            value += '';
            break;

          default:
            value = '';
        }
        if (!allowHtml) {
          value = value.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;"); //escape html special chars
        }
        td.innerHTML = value.replace(/\n/g, '<br/>');
        self.minWidthFix(td);
        datamap.set(row, col, value);
        grid.updateLegend({row: row, col: col});
      }
      if (refreshRows) {
        self.blockedCols.refresh();
      }
      if (refreshCols) {
        self.blockedRows.refresh();
      }
      var recreated = grid.keepEmptyRows();
      if (!recreated) {
        selection.refreshBorders();
      }
//    setTimeout(function () {
//      if (!refreshRows) {
//        self.blockedRows.dimensions(changes);
//      }
//      if (!refreshCols) {
//        self.blockedCols.dimensions(changes);
//      }
//    }, 10);
      if (changes.length) {
        self.container.triggerHandler("datachange.handsontable", [changes, source || 'edit']);
      }
      return td;
    };

    /**
     * Returns current selection. Returns undefined if there is no selection.
     * @public
     * @return {Array} [topLeftRow, topLeftCol, bottomRightRow, bottomRightCol]
     */
    this.getSelected = function () { //https://github.com/warpech/jquery-handsontable/issues/44  //cjl
      if (selection.isSelected()) {
        var coords = grid.getCornerCoords([priv.selStart, priv.selEnd]);
        return [coords.TL.row, coords.TL.col, coords.BR.row, coords.BR.col];
      }
    };

    /**
     * Load data from array
     * @public
     * @param {Array} data
     * @param {Boolean} [allowHtml]
     */
    this.loadData = function (data, allowHtml) {
      priv.isPopulated = false;
      datamap.clear();
      grid.clear();
      grid.populateFromArray({
        row: 0,
        col: 0
      }, data, null, allowHtml, 'loadData');
      priv.isPopulated = true;
      self.clearUndo();
    };
    this.selectStyle =function(){
    	$($('#rowHeaderDiv').find('th')).css('background-color','');
    	$($('#colHeaderDiv').find('th')).css('background-color','');
    	var srow = self.getSelected()[0];
		var erow = self.getSelected()[2];
		var scol = self.getSelected()[1];
		var ecol = self.getSelected()[3];
		if(srow==erow&&scol==ecol){
			var td = grid.getCellAtCoords({row:srow,col:scol});
			var rowspan = parseInt($(td).attr('rowspan'));
			var colspan = parseInt($(td).attr('colspan'));
			if(rowspan||colspan){
				erow = erow + rowspan -1;
				ecol = ecol + colspan -1;
			}
		}
		for(var i=srow;i<=erow;i++){
			$($('#rowHeaderDiv').find('th')[i+1]).css('background-color','#e1e1e1');
		}
		for(var i=scol;i<=ecol;i++){
			$($('#colHeaderDiv').find('th')[i+1]).css('background-color','#e1e1e1');
		}
  	};
    /**
	 * 是否选择单独单元格
	 */
	this.isOnlyCell = function(){
		var coords = self.selectedCoords;
		if(coords[0]==coords[2]&&coords[1]==coords[3]) 
			return true;
		else
			return false;
	}
    /**
	 * 是否存在合并单元格
	 */
	this.isMergecells = function(){
		var coords = self.getSelected();
		if(typeof(coords) == "undefined") return false;
		for(var r = coords[2];r>=coords[0];r--){
        	for(var c =coords[3];c>=coords[1];c--){
        		var $td = $(grid.getCellAtCoords({row: r,col: c}));
        		var rowspan = $td.attr('rowspan');
				var colspan = $td.attr('colspan');
				if((rowspan&&rowspan!=1)||(colspan&&colspan!=1)){
					return true;
				}
//				if($td.is(":hidden")){
//					return true;
//				}
        	}
        }
		return false;
	}
	 /**
	 * 是否存在隐藏的单元格
	 */
	this.isHiddenCells = function(){
		var coords = self.getSelected();
		if(typeof(coords) == "undefined") return false;
		for(var r = coords[2];r>=coords[0];r--){
        	for(var c =coords[3];c>=coords[1];c--){
        		var $td = $(grid.getCellAtCoords({row: r,col: c}));
				if($td.is(":hidden")){
					return true;
				}
        	}
        }
		return false;
	}
	/**
	 * 合并单元格
	 */
	this.Mergecells = function(){
		var temp = self.selectedCoords;
		if(self.isOnlyCell()) return;
		if(self.isMergecells()||self.isHiddenCells()) return;
        for(var r = temp[2];r>=temp[0];r--){
        	for(var c =temp[3];c>=temp[1];c--){
        		var td = grid.getCellAtCoords({row: r,col: c});
        		if(r!=temp[0]||c!=temp[1]){
        			var arrTh = $('#dataTable table thead').find('th');
        			var t = parseInt($(arrTh[c+1]).css('width').split('p')[0])-50;
//        			$(td).attr('name',$(td).offset().top+","+$(td).offset().left+","+temp[0]+"#"+temp[1]).html("").hide();
        			$(td).attr('name',parseInt($('#demo').scrollTop()+$(td).offset().top)+","+parseInt($('#demo').scrollLeft()+$(td).offset().left+t)+","+temp[0]+"#"+temp[1]).html("").hide();
        		}
        	}
        }
        var td = grid.getCellAtCoords({
          row: temp[0],
          col: temp[1]
        });
        $(td).attr('rowspan',temp[2]-temp[0]+1);
        $(td).attr('colspan',temp[3]-temp[1]+1);
	}
	/**
	 * 打开单元格
	 */
	this.MergecellsOpen = function(){
		var temp = self.selectedCoords;
		if(self.isOnlyCell()){
			var $seledCell = $(grid.getCellAtCoords({row: temp[0],col: temp[1]}));
			var rowspan = parseInt($seledCell.attr('rowspan'));
			var colspan = parseInt($seledCell.attr('colspan'));
			for(var r = parseInt(temp[0])+rowspan-1;r>=temp[0];r--){
				for(var c = parseInt(temp[1])+colspan-1;c>=temp[1];c--){
					if(r==temp[0]&&c==temp[1]){
						$seledCell.attr('rowspan',1).attr('colspan',1);
					}else{
						var htd = grid.getCellAtCoords({row: r,col: c});
        				$(htd).removeAttr("name").show();
					}
				}
			}
		}else{
			for(var r = temp[2];r>=temp[0];r--){
        		for(var c =temp[3];c>=temp[1];c--){
        			var $td = $(grid.getCellAtCoords({row: r,col: c}));
        			var rowspan = parseInt($td.attr('rowspan'));
					var colspan = parseInt($td.attr('colspan'));
					for(var rs = r+rowspan-1;rs>=r;rs--){
						for(var cs = c+colspan-1;cs>=c;cs--){
							if(rs==r&&cs==c){
								$td.attr('rowspan',1).attr('colspan',1);
							}else{
								var htd = grid.getCellAtCoords({row: rs,col: cs});
        						$(htd).removeAttr("name").show();
							}
						}
					}
        		}
        	}
		}
	}
	//前插入行
	this.row_above=function(){var coords = grid.getCornerCoords([priv.selStart, priv.selEnd]);grid.alter("insert_row", coords.TL);}
	//后插入行
	this.row_below=function(){var coords = grid.getCornerCoords([priv.selStart, priv.selEnd]);isTHMenu = true;grid.alter("insert_row", {row: coords.BR.row + 1, col: 0});}
	//左插入列
	this.col_left=function(){var coords = grid.getCornerCoords([priv.selStart, priv.selEnd]);grid.alter("insert_col", coords.TL);}
	//右插入列
	this.col_right=function(){var coords = grid.getCornerCoords([priv.selStart, priv.selEnd]);isTHMenu = true;grid.alter("insert_col", {row: 0, col: coords.BR.col + 1});}
	//删除行列
	this.remove=function(key){var coords = grid.getCornerCoords([priv.selStart, priv.selEnd]);grid.alter(key, coords.TL, coords.BR);}
	 /**
       * 根据隐藏td 找左上角td
       */
	this.findTDByhidden = function(td){
		if(!$(td).is(":hidden")) return td;
		
		var arr = $(td).attr('name').split(',')[2].split('#');
		var coords = grid.getCellCoords(td);
		coords.row=parseInt(arr[0]);
		coords.col=parseInt(arr[1]);
//		while($(td).is(":hidden")){
// 			coords.row -=1;
// 			td = grid.getCellAtCoords(coords);
//		}
//		if(!$(td).attr('rowspan')){
//			coords.row +=1;
//		}
//			td = grid.getCellAtCoords(coords);
//		while($(td).is(":hidden")){
// 			coords.col -=1;
// 			td = grid.getCellAtCoords(coords);
//		}
//		if(!$(td).attr('colspan')){
//			coords.col +=1;
//		}
		
		return grid.getCellAtCoords(coords);
	}
	 /**
       * 递归选择样式
       */
	this.onFor = function(selStart,selEnd){
      	for(var r = selEnd.row;r>=selStart.row;r--){
			for(var c = selEnd.col;c>=selStart.col;c--){
				var td = grid.getCellAtCoords({row:r,col:c});
				var rowspan = $(td).attr('rowspan');
				var colspan = $(td).attr('colspan');
				if($(td).is(':hidden')){
					var showtd = self.findTDByhidden(td);
					var coords = grid.getCellCoords(showtd);
					var maxRow = coords.row+parseInt($(showtd).attr('rowspan'))-1;
					var maxCol = coords.col+parseInt($(showtd).attr('colspan'))-1;
					if(coords.row<selStart.row){
						selStart.row = coords.row;
					}
					if(coords.col<selStart.col){
						selStart.col = coords.col;
					}
					if(maxRow>selEnd.row){
						selEnd.row = maxRow;
						self.onFor(selStart,selEnd);
						return {selStart:selStart,selEnd:selEnd};
					}
					if(maxCol>selEnd.col){
						selEnd.col = maxCol;
						self.onFor(selStart,selEnd);
						return {selStart:selStart,selEnd:selEnd};
					}
				}else if(rowspan||colspan){
					var maxRow = r+parseInt(rowspan)-1;
					var maxCol = c+parseInt(colspan)-1;
					if(maxRow>selEnd.row){
						selEnd.row = maxRow;
						self.onFor(selStart,selEnd);
						return {selStart:selStart,selEnd:selEnd};
					}
					if(maxCol>selEnd.col){
						selEnd.col = maxCol;
						self.onFor(selStart,selEnd);
						return {selStart:selStart,selEnd:selEnd};
					}
				}
			}
    	 }
      	return {selStart:selStart,selEnd:selEnd};
	}
	//TODO
	this.rowHidde = function(){
		var srow = this.getSelected()[0];
		var erow = this.getSelected()[2];
		for(var i=srow;i<=erow;i++){
			$($('#rowHeaderDiv').find('th')[i+1]).attr('name','visibility').css('background-color','#E8776C');
		}
  		var XMLData = Handsontable.XML.parsingToXML();
  		$tableModel.val(XMLData);
	}
	this.colHidde = function(){
		var scol = this.getSelected()[1];
		var ecol = this.getSelected()[3];
		for(var i=scol;i<=ecol;i++){
			$($('#colHeaderDiv').find('th')[i+1]).attr('name','visibility').css('background-color','#E8776C');
		}
		var XMLData = Handsontable.XML.parsingToXML();
  		$tableModel.val(XMLData);
	}
	this.rowShow = function(){
		var srow = this.getSelected()[0];
		var erow = this.getSelected()[2];
		for(var i=srow;i<=erow;i++){
			$($('#rowHeaderDiv').find('th')[i+1]).attr('name',null).css('background-color','#EEEEEE');
		}
  		var XMLData = Handsontable.XML.parsingToXML();
  		$tableModel.val(XMLData);
	}
	this.colShow = function(){
		var scol = this.getSelected()[1];
		var ecol = this.getSelected()[3];
		for(var i=scol;i<=ecol;i++){
			$($('#colHeaderDiv').find('th')[i+1]).attr('name','null').css('background-color','#EEEEEE');
		}
		var XMLData = Handsontable.XML.parsingToXML();
  		$tableModel.val(XMLData);
	}
    /**
     * Return 2-dimensional array with the current grid data
     * @public
     * @param {Boolean} [asReference=false] If TRUE, function will return direct reference to the internal data array. That is faster but should be used only to READ data (otherwise you will mess up the DOM table). To write data, you should always use method `setDataAtCell`.
     * @return {Array}
     */
    this.getData = function (asReference) {
      if (asReference === true) {
        return datamap.getAll();
      }
      else {
        return $.extend(true, [], datamap.getAll());
      }
    };

    /**
     * Refreshes the legend for a cell, row, col, or entire table
     * @public
     * @param {Number} [row] - Optional to update a single row
     * @param {Number} [col] - Optional to update a single col
     */
    this.refreshLegend = function (row, col) {
      var rowLen, colLen, x, xLen, y, yLen;
      if (typeof row !== "undefined" && row !== null) {
        rowLen = row + 1;
      } else {
        row = 0;
        rowLen = self.rowCount;
      }
      if (typeof col !== "undefined" && col !== null) {
        colLen = col + 1;
      } else {
        col = 0;
        colLen = self.colCount;
      }
      for (x = row, xLen = rowLen; x < xLen; x += 1) {
        for (y = col, yLen = colLen; y < yLen; y += 1) {
          grid.updateLegend({row: x, col: y});
        }
      }
      priv.legendDirty = false;
    };

    /**
     * Update settings
     * @public
     */
    this.updateSettings = function (settings) {
      var i, j;
	  
      if (typeof settings.fillHandle !== "undefined") {
        if (priv.fillHandle && settings.fillHandle === false) {
          autofill.disable();
        }
        else if (!priv.fillHandle && settings.fillHandle === true) {
          autofill.init();
        }
      }

      if (typeof settings.undo !== "undefined") {
        if (priv.undoRedo && settings.undo === false) {
          priv.undoRedo = null;
        }
        else if (!priv.undoRedo && settings.undo === true) {
          priv.undoRedo = new Handsontable.UndoRedo(self);
        }
      }

      if (!self.blockedCols) {
        self.blockedCols = new Handsontable.BlockedCols(self);
        self.blockedRows = new Handsontable.BlockedRows(self);
      }

      if (typeof settings.legend !== "undefined") {
        priv.legendDirty = true;
      }

      for (i in settings) {
        if (settings.hasOwnProperty(i)) {
          priv.settings[i] = settings[i];

          //launch extensions
          if (Handsontable.extension[i]) {
            priv.extensions[i] = new Handsontable.extension[i](self, settings[i]);
          }
        }
      }

      if (typeof settings.colHeaders !== "undefined") {
        if (settings.colHeaders === false && priv.extensions["ColHeader"]) {
          priv.extensions["ColHeader"].destroy();
        }
        else {
          priv.extensions["ColHeader"] = new Handsontable.ColHeader(self, settings.colHeaders);
        }
      }

      if (typeof settings.rowHeaders !== "undefined") {
        if (settings.rowHeaders === false && priv.extensions["RowHeader"]) {
          priv.extensions["RowHeader"].destroy();
        }
        else {
          priv.extensions["RowHeader"] = new Handsontable.RowHeader(self, settings.rowHeaders);
        }
      }

      var blockedRowsCount = self.blockedRows.count();
      var blockedColsCount = self.blockedCols.count();
      if (blockedRowsCount && blockedColsCount && (typeof settings.rowHeaders !== "undefined" || typeof settings.colHeaders !== "undefined")) {
        if (priv.cornerHeader) {
          priv.cornerHeader.remove();
          priv.cornerHeader = null;
        }

        var position = self.table.position();
        self.positionFix(position);

        var div = document.createElement('div');
        div.style.position = 'absolute';
        div.style.top = position.top + 'px';
        div.style.left = position.left + 'px';

        var table = document.createElement('table');
        table.cellPadding = 0;
        table.cellSpacing = 0;
        div.appendChild(table);

        var thead = document.createElement('thead');
        table.appendChild(thead);

        var tr, th;
        for (i = 0; i < blockedRowsCount; i++) {
          tr = document.createElement('tr');
          for (j = blockedColsCount - 1; j >= 0; j--) {
            th = document.createElement('th');
            th.className = self.blockedCols.headers[j].className;
            th.innerHTML = self.blockedCols.headerText('&nbsp;');
            self.minWidthFix(th);
            tr.appendChild(th);
          }
          thead.appendChild(tr);
        }
        priv.cornerHeader = $(div);
        priv.cornerHeader.on('click', function () {
          selection.selectAll();
        });
        container.append(priv.cornerHeader);
      }
      else {
        if (priv.cornerHeader) {
          priv.cornerHeader.remove();
          priv.cornerHeader = null;
        }
      }

      self.blockedCols.update();
      self.blockedRows.update();

      var recreated = grid.keepEmptyRows();
      if (!recreated) {
        selection.refreshBorders();
      }

      if (priv.isPopulated && priv.legendDirty) {
        self.refreshLegend();
      }
      thdrafting.init();
    };

    /**
     * Clears grid
     * @public
     */
    this.clear = function () {
      selection.selectAll();
      selection.empty();
    };

    /**
     * Clears undo history
     * @public
     */
    this.clearUndo = function () {
      priv.undoRedo && priv.undoRedo.clear();
    };

    /**
     * Alters the grid
     * @param {String} action See grid.alter for possible values
     * @param {Number} from
     * @param {Number} [to] Optional. Used only for actions "" and "remove_col"
     * @public
     */
    this.alter = function (action, from, to) {
      if (typeof to === "undefined") {
        to = from;
      }
      switch (action) {
        case "insert_row":
        case "remove_row":
          grid.alter(action, {row: from, col: 0}, {row: to, col: 0});
          break;

        case "insert_col":
        case "remove_col":
          grid.alter(action, {row: 0, col: from}, {row: 0, col: to});
          break;

        default:
          throw Error('There is no such action "' + action + '"');
          break;
      }
    };

    /**
     * Returns <td> element corresponding to params row, col
     * @param {Number} row
     * @param {Number} col
     * @public
     * @return {Element}
     */
    this.getCell = function (row, col) {
      return grid.getCellAtCoords({row: row, col: col});
    };

    /**
     * Returns value corresponding to cell at row, col
     * @param {Number} row
     * @param {Number} col
     * @public
     * @return {string}
     */
    this.getDataAtCell = function (row, col) {
      return datamap.get(row, col);
    };

    /**
     * Returns cell meta data object corresponding to params row, col
     * @param {Number} row
     * @param {Number} col
     * @public
     * @return {Object}
     */
    this.getCellMeta = function (row, col) {
      return {
        isWritable: grid.isCellWritable($(grid.getCellAtCoords({row: row, col: col})))
      }
    };

    /**
     * Sets cell to be readonly
     * @param {Number} row
     * @param {Number} col
     * @public
     */
    this.setCellReadOnly = function (row, col) {
      $(grid.getCellAtCoords({row: row, col: col})).data("readOnly", true);
    };

    /**
     * Sets cell to be editable (removes readonly)
     * @param {Number} row
     * @param {Number} col
     * @public
     */
    this.setCellEditable = function (row, col) {
      $(grid.getCellAtCoords({row: row, col: col})).data("readOnly", false);
    };

    /**
     * Returns headers (if they are enabled)
     * @param {Object} obj Instance of rowHeader or colHeader
     * @param {Number} count Number of rows or cols
     * @param {Number} index (Optional) Will return only header at given index
     * @return {Array|String}
     */
    var getHeaderText = function (obj, count, index) {
      if (obj) {
        if (typeof index !== 'undefined') {
          return obj.columnLabel(index);
        }
        else {
          var headers = [];
          for (var i = 0; i < count; i++) {
            headers.push(obj.columnLabel(i));
          }
          return headers;
        }
      }
    };

    /**
     * Return array of row headers (if they are enabled). If param `row` given, return header at given row as string
     * @param {Number} row (Optional)
     * @return {Array|String}
     */
    this.getRowHeader = function (row) {
      return getHeaderText(self.rowHeader, self.rowCount, row);
    };

    /**
     * Return array of col headers (if they are enabled). If param `col` given, return header at given col as string
     * @param {Number} col (Optional)
     * @return {Array|String}
     */
    this.getColHeader = function (col) {
      return getHeaderText(self.colHeader, self.colCount, col);
    };

    /**
     * Selects cell on grid. Optionally selects range to another cell
     * @param {Number} row
     * @param {Number} col
     * @param {Number} [endRow]
     * @param {Number} [endCol]
     * @param {Boolean} [scrollToCell=true] If true, viewport will be scrolled to the selection
     * @public
     */
    this.selectCell = function (row, col, endRow, endCol, scrollToCell) {
//      selection.start({row: row, col: col});
//       startCellCoord = {row: row, col: col};
      if (typeof endRow === "undefined") {
        selection.setRangeEnd(self.getCell(row, col), scrollToCell);
      }
      else {
//    	priv.currentBorder.appear([{row:row,col:col},{row:endRow,col:endCol}]);
    	priv.fillHandle.appear([{row:row,col:col}, {row:endRow,col:endCol}]);
		priv.selectionBorder.appear([{row:row,col:col}, {row:endRow,col:endCol}]);
		priv.selStart.row = row;
		priv.selStart.col = col;
		priv.selEnd.row = endRow;
		priv.selEnd.col = endCol;
		  
//        selection.setRangeEnd(self.getCell(endRow, endCol), scrollToCell);
      }
    };
    this.removeSelectStyle = function(){
    	$($('#rowHeaderDiv').find('th')).css('background-color','');
    	$($('#colHeaderDiv').find('th')).css('background-color','');
  	};
    this.selectAllCell = function(){
        if (!priv.settings.multiSelect) {
            return;
          }
          var tds = grid.getAllCells();
          if (tds.length) {
            var s = grid.getCellCoords(tds[0]);
            var e = grid.getCellCoords(tds[tds.length - 1]);
            priv.fillHandle.appear([{row:s.row,col:s.col}, {row:e.row,col:e.col}]);
    		priv.selectionBorder.appear([{row:s.row,col:s.col}, {row:e.row,col:e.col}]);
          }
        };
    /**
     * Deselects current sell selection on grid
     * @public
     */
    this.deselectCell = function () {
      selection.deselect();
    };

    /**
     * Create DOM elements for selection border lines (top, right, bottom, left) and optionally background
     * @constructor
     * @param {jQuery} $container jQuery DOM element of handsontable container
     * @param {Object} options Configurable options
     * @param {Boolean} [options.bg] Should include a background
     * @param {String} [options.className] CSS class for border elements
     */
    function Border($container, options) {
      this.$container = $container;
      var container = this.$container[0];

      if (options.bg) {
        this.bg = document.createElement("div");
        this.bg.className = 'htBorderBg ' + options.className;
        if(options.className=='selection'){
        	var $selected = $('<div></div>');
            $selected.addClass('select-inner');
            $(this.bg).append($selected);
        }
        container.insertBefore(this.bg, container.firstChild);
      }

      this.main = document.createElement("div");
      this.main.style.position = 'absolute';
      this.main.style.top = 0;
      this.main.style.left = 0;
      this.main.innerHTML = (new Array(5)).join('<div class="htBorder ' + options.className + '"></div>');
      this.disappear();
      container.appendChild(this.main);

      var nodes = this.main.childNodes;
      this.top = nodes[0];
      this.left = nodes[1];
      this.bottom = nodes[2];
      this.right = nodes[3];

      this.borderWidth = $(this.left).width();
    }

    Border.prototype = {
      /**
       * Show border around one or many cells
       * @param {Object[]} coordsArr
       */
      appear: function (coordsArr) {
        var $from, $to, fromOffset, toOffset, containerOffset, top, minTop, left, minLeft, height, width;
        if (this.disabled) {
          return;
        }

        this.corners = grid.getCornerCoords(coordsArr);

        $from = $(grid.getCellAtCoords(this.corners.TL));
        $to = (coordsArr.length > 1) ? $(grid.getCellAtCoords(this.corners.BR)) : $from;
        fromOffset = $from.offset();
        var outerHeight = parseInt($to.css('height').split('p')[0]);
        var outerWidth =  $to.outerWidth();
        if($from.is(':hidden')){
        	fromOffset.top = parseInt($from.attr('name').split(',')[0])-$('#demo').scrollTop();
        	fromOffset.left = parseInt($from.attr('name').split(',')[1])-$('#demo').scrollLeft();
        }
        toOffset = (coordsArr.length > 1) ? $to.offset() : fromOffset;
        if(isThHander ==true){
        	if($to.attr('rowspan')||$to.attr('colspan')){
        		outerHeight = outerHeight/parseInt($to.attr('rowspan'));
        		outerWidth = outerWidth/parseInt($to.attr('colspan'));
        	}
        }
        if($to.is(':hidden')){
        	toOffset.top = parseInt($to.attr('name').split(',')[0])-$('#demo').scrollTop();
        	toOffset.left = parseInt($to.attr('name').split(',')[1])-$('#demo').scrollLeft();
        }
        containerOffset = this.$container.offset();

        minTop = fromOffset.top;
        height = toOffset.top + outerHeight - minTop;
        minLeft = fromOffset.left;
        width = toOffset.left + outerWidth - minLeft;

        top = minTop - containerOffset.top + this.$container.scrollTop() - 1;
        left = minLeft - containerOffset.left + this.$container.scrollLeft() - 1;

        if (parseInt($from.css('border-top-width')) > 0) {
          top += 1;
          height -= 1;
        }
        if (parseInt($from.css('border-left-width')) > 0) {
          left += 1;
          width -= 1;
        }

        if (top < 0) {
          top = 0;
        }
        if (left < 0) {
          left = 0;
        }

        if (this.bg) {
          this.bg.style.top = top + 'px';
          this.bg.style.left = left + 'px';
          this.bg.style.width = width - 4  + 'px';
          this.bg.style.height = height - 4  + 'px';
          this.bg.style.display = 'block';
        }

        this.top.style.top = top + 'px';
        this.top.style.left = left + 'px';
        this.top.style.width = width + 'px';

        this.left.style.top = top + 'px';
        this.left.style.left = left + 'px';
        this.left.style.height = height + 'px';

        var delta = Math.floor(this.borderWidth / 2);

        this.bottom.style.top = top + height - delta + 'px';
        this.bottom.style.left = left + 'px';
        this.bottom.style.width = width + 'px';

        this.right.style.top = top + 'px';
        this.right.style.left = left + width - delta + 'px';
        this.right.style.height = height + 1 + 'px';

        this.main.style.display = 'block';
      },

      /**
       * Hide border
       */
      disappear: function () {
        this.main.style.display = 'none';
        if (this.bg) {
          this.bg.style.display = 'none';
        }
        this.corners = null;
      }
    };

    /**
     * Create DOM element for drag-down handle
     * @constructor
     * @param {jQuery} $container jQuery DOM element of handsontable container
     */
    function FillHandle($container) {
      this.$container = $container;
      var container = this.$container[0];

      this.handle = document.createElement("div");
      this.handle.className = "htFillHandle";
      this.disappear();
      container.appendChild(this.handle);

      var that = this;
//    $(this.handle).mousedown(function () {
//      that.isDragged = 1;
//    });
    }

    FillHandle.prototype = {
      /**
       * Show handle in cell corner
       * @param {Object[]} coordsArr
       */
      appear: function (coordsArr) {
        if (this.disabled) {
          return;
        }
		
        var $td, tdOffset, containerOffset, top, left, height, width;

        var corners = grid.getCornerCoords(coordsArr);
        self.handleEnd = corners.BR;
        $td = $(grid.getCellAtCoords(corners.BR));
        height = $td.outerHeight();
        width = $td.outerWidth();
        tdOffset = $td.offset();
         if(isThHander ==true){
        	if($td.attr('rowspan')||$td.attr('colspan')){
        		height = height/parseInt($td.attr('rowspan'));
        		width = width/parseInt($td.attr('colspan'));
        	}else{
        		
        	}
        }
        if($td.is(':hidden')){
        	tdOffset.top = parseInt($td.attr('name').split(',')[0])-$('#demo').scrollTop();
        	tdOffset.left = parseInt($td.attr('name').split(',')[1])-$('#demo').scrollLeft();
        }
        containerOffset = this.$container.offset();

        top = tdOffset.top - containerOffset.top + this.$container.scrollTop() - 1;
        left = tdOffset.left - containerOffset.left + this.$container.scrollLeft() - 1;

        this.handle.style.top = top + height - 3 + 'px';
        this.handle.style.left = left + width - 3 + 'px';
        this.handle.style.display = 'block';
      },

      /**
       * Hide handle
       */
      disappear: function () {
        this.handle.style.display = 'none';
      }
    };
    
    thdrafting={
    	init:function(){
    		var startX,startY,endX,endY;
    		var $th;
    		var thWidth=50,divWdith,thHeight=22,divHeight;
    		var $t = $('#colHeaderDiv').find('.c-active');
       		$t.removeClass('c-active');
       		$($("#colHeaderDiv th[class='htColHeader htRowHeader']")[0]).css('min-width',''); 
    		var colHeaders= $("#colHeaderDiv th[class='htColHeader']"); 
//  		for(var i=0;i<colHeaders.length;i++){
    		$(colHeaders).each(function(index) {
//  			if(index>1){
    				$(this).css('height','22px')
//  				    .css('min-width','')
    				.css('position','relative');
    				var $line = $('<div></div>');
    				$line.css('position','absolute')
//  				.css('background','red')
    				.css('width','8px')
    				.css('height','26px')
    				.css('top',0)
    				.css('left',parseInt($(this).css('width').split('p')[0])+5)
    				.css('cursor','col-resize');
    				$(this).append($line);
    				$line.bind('mousedown',function(event){
    					isMoveDown = true;
    					selection.deselect();
    					changeCol = index;
    					$th = $(this).parent();
    					thWidth = parseInt($th.css('min-width').split('p')[0]);
    					divWdith = parseInt($('#dataTable').css('width').split('p')[0]);
    					startX=event.clientX;
    					var arrTh = $('#dataTable table thead').find('th');
    					$('body').bind('mousemove',function(e){
    						isChangeWidth = true;
							endX=e.clientX;
							endY=e.clientY;
							if((thWidth+endX-startX)>=50){
								etX = endX-startX;
								var width = thWidth+endX-startX;
								$('#dataTable').css('width',divWdith+endX-startX);
								$('#colHeaderDiv').css('width',divWdith+endX-startX);
								$th.css('min-width',width);
								$th.find('div').css('left',width+5);
								$('body').css('cursor','w-resize');
								var rowHeader= $("#rowHeaderDiv th[class='htRowHeader']");
								for(var i=0;;i++){
									var td = grid.getCellAtCoords({row:i,col:changeCol});
									if(!td){
										break;
									}
//									if($(td).is(':hidden')){
//										var name = $(td).attr('name');
//										var left = parseInt(name.split(',')[1])+endX-startX;
//										$("#XMLName").val(left);
//										$(td).attr('name',name.split(',')[0]+","+left+","+name.split(',')[2]).html("").hide();
//									}
									$(arrTh[changeCol+1]).css('width',width);
									var tmpHeight = parseInt($(td).parent().css('height').split('p')[0]);
									if(i==0){
										tmpHeight = tmpHeight-2;
									}else{
										tmpHeight = tmpHeight-1;
									}
//									$(td).parent().css('height',tmpHeight-1); 
	        					 	$(rowHeader[i+1]).parent().css('height',tmpHeight);
	        					 	$(rowHeader[i+1]).css('height',tmpHeight); 
	        					 	$(rowHeader[i+1]).find('div').css('top',tmpHeight-5);
								}
							}
    					});
    				});
//  			}
			});
			var $t = $('#rowHeaderDiv').find('.r-active');
       		$t.removeClass('r-active');
    		var rowHeader= $("#rowHeaderDiv th[class='htRowHeader']"); 
    		$(rowHeader).each(function(index) {
    			var h = $(grid.getCellAtCoords({row:index-1,col:0})).parent().css('height');
    			if(index==0){
    				h="22px";
    			}else if(index==1){
    				h = parseInt(h.split('p')[0])-2;
    			}else{
    				h = parseInt(h.split('p')[0])-1;
    			}
    			$(this).css('height',h)
    			.css('position','relative');
    			var $line = $('<div></div>');
    			$line.css('position','absolute')
//  			.css('background','red')
    			.css('width','58px')
    			.css('height','8px')
    			.css('top',h-4)
    			.css('left',0)
    			.css('cursor','row-resize');
    			$(this).append($line);
    			$line.bind('mousedown',function(event){
    				isMoveDown = true;
    				selection.deselect();
    				changeRow = index-1;
    				$th = $(this).parent();
    				thHeight = parseInt($th.css('height').split('p')[0]);
    				divHeight = parseInt($('#dataTable').css('height').split('p')[0]);
					startY=event.clientY;
					$('body').bind('mousemove',function(e){
						endX=e.clientX;
						endY=e.clientY;
						isChangeHeight = true;
						if((thHeight+endY-startY)>=22){
							etY = endY-startY;
							var height = thHeight+endY-startY;
							$('#dataTable').css('height',divHeight+endY-startY);
							$('#rowHeaderDiv').css('height',divHeight+endY-startY);
							$th.css('height',height);
							$th.parent().css('height',height);
							$th.find('div').css('top',height-5);
							$('body').css('cursor','s-resize');
							var td = grid.getCellAtCoords({row:changeRow,col:0});
							if(changeRow==0){
								$(td).parent().css('height',height+2); 
							}else{
								$(td).parent().css('height',height+1); 
							}
						}
    				});
    			});
			});
//			$('body').bind('mousemove',function(event){
//				endX=event.clientX;
//				endY=event.clientY;
//  			if(isChangeWidth){
//					if((thWidth+endX-startX)>=50){
//						var width = thWidth+endX-startX;
//						$('#dataTable').css('width',divWdith+endX-startX);
//						$('#colHeaderDiv').css('width',divWdith+endX-startX);
//						$th.css('width',width);
//						$th.find('div').css('left',width+6);
//						$('body').css('cursor','w-resize');
//						for(var i=0;;i++){
//							var td = grid.getCellAtCoords({row:i,col:changeCol});
//							if(!td){
//								break;
//							}
//							$(td).css('width',width);
//						}
//					}
//  			}else if(isChangeHeight){
//					if((thHeight+endY-startY)>=22){
//						var height = thHeight+endY-startY;
//						$('#dataTable').css('height',divHeight+endY-startY);
//						$('#rowHeaderDiv').css('height',divHeight+endY-startY);
//						$th.css('height',height);
//						$th.find('div').css('top',height-3);
//						$('body').css('cursor','s-resize');
////						for(var i=0;;i++){
////							var td = grid.getCellAtCoords({row:changeRow,col:i});
////							if(!td){
////								break;
////							}
////							$(td).css('height',height);
////						}
//						var td = grid.getCellAtCoords({row:changeRow,col:0});
//						$(td).parent().css('height',height+1); 
//					}
//  			}
//  		});
			$('body').bind('mouseup',function(){
				$('body').unbind("mousemove");
				isMoveDown = false;
				if(isChangeWidth==false&&isChangeHeight==false) return;
				if(isChangeWidth){
					for(var i=0;i<self.rowCount;i++){
						for(var j=changeCol;;j++){
							var td = grid.getCellAtCoords({row:i,col:j});
							if(!td){
								break;
							}
							if($(td).is(':hidden')){
								if(i!=0||j!=changeCol){
									var name = $(td).attr('name');
									var left = parseInt(name.split(',')[1])+etX;
									$(td).attr('name',name.split(',')[0]+","+left+","+name.split(',')[2]);
								}else{
									
								}
							}
						}
					}
				}else if(isChangeHeight){
					for(var i=changeRow;i<self.rowCount;i++){
						for(var j=0;;j++){
							var td = grid.getCellAtCoords({row:i,col:j});
							if(!td){
								break;
							}
							if(i==0) continue;
							if($(td).is(':hidden')){
								if(i!=changeRow||j!=0){
									var name = $(td).attr('name');
									var top = parseInt(name.split(',')[0])+etY;
									$(td).attr('name',top+","+name.split(',')[1]+","+name.split(',')[2]);
								}else{
									
								}
							}
						}
					}
				}
    			isChangeWidth = false;
    			isChangeHeight = false;
    			if(selection.isSelected()){
    				priv.selectionBorder.appear([{row:priv.selStart.row,col:priv.selStart.col}, {row:priv.selEnd.row,col:priv.selEnd.col}]);
					priv.fillHandle.appear([{row:priv.selStart.row,col:priv.selStart.col}, {row:priv.selEnd.row,col:priv.selEnd.col}]);
    			}
					//TODO 重新解析table 存入cookie
          		var XMLData = Handsontable.XML.parsingToXML();
          		$tableModel.val(XMLData);
				$('body').css('cursor','default');
    		});
    	}
    }
    /**
     * 样式
     */
    Handsontable.tableStyle=function(type,value){
    	if(!selection.isSelected()) return;
    	for(var r=priv.selStart.row;r<=priv.selEnd.row;r++){
    			for(var c=priv.selStart.col;c<=priv.selEnd.col;c++){
    				var $td = $(grid.getCellAtCoords({row:r,col:c}));
    				if(!$td.is(":hidden")){
    					 switch (type) {
    					 	case "isselect":
    					 		$td.parent().attr('isselect',value);
    						 break;
    					 	case "wordWrap":
    					 		$td.css('white-space',value);
    					 		var rowHeaders= $("#rowHeaderDiv th[class='htRowHeader']");
    					 		if(value=="nowrap"){
    					 			$td.parent().css('height','22px'); 
    					 			$(rowHeaders.get(r+1)).parent().css('height','22px'); 
    					 			$(rowHeaders.get(r+1)).css('height','22px'); 
    					 			$(rowHeaders.get(r+1)).find('div').css('top','17px');
    					 		}else{
    					 			$td.parent().css('height',$td.css('height')); 
        					 		$(rowHeaders.get(r+1)).parent().css('height',$td.css('height'));
        					 		$(rowHeaders.get(r+1)).css('height',$td.css('height')); 
        					 		$(rowHeaders.get(r+1)).find('div').css('top',parseInt($td.css('height').split('p')[0])-5);
    					 		}
    					 	break;
    					 	case "text":
    					 		if($td.html().indexOf('</a>')!=-1){
    					 			$td.html('<a href="javascript:void(0);">'+value+'</a>');
    					 		}else{
    					 			$td.html(value);
    					 		}
//    					 		setData.push([r, c, value, undefined]);
      						break;
    					 	case "fontFamily":
          						$td.css('font-family',value);
         						break;
         					case "fontSize":
         						$td.css('font-size',value+"px");
          						break;
          					case "fontColor":
          						$td.css('color',value);
          						break;
          					case "fontWeight":
          						$td.css('font-weight',value);
          						break;
          					case "fontStyle":
          						$td.css('font-style',value);
          						break;
          					case "textDecoration":
          						$td.css('text-decoration',value);
          						break;
          					case "align":
          						$td.css('text-align',value);
         						break;
          					case "valign":
          						$td.css('vertical-align',value);
         						break;
          					case "height":
          						var th = $('#rowHeaderDiv').find('th').get(r+1);
//          						if(r==0){
//          							$(th).css('height',value-2+"px");
//          						}else{
//          							$(th).css('height',value-1+"px");
//          						}
          						$(th).find('div').css('top',value-4);
          						$td.parent().css('height',value);
          						selection.refreshBorders();
          						break;
          					case "width":
//									var oldWidth = parseInt($td.css('min-width').split('p')[0]);
//									var divWdith = parseInt($('#colHeaderDiv').css('width').split('p')[0]);
//									$('#dataTable').css('width',divWdith-oldWidth+parseInt(value));
//									$('#colHeaderDiv').css('width',divWdith-oldWidth+value);
									var th = $('#colHeaderDiv').find('th').get(c+1);
									$(th).css('min-width',value+"px");
									$(th).find('div').css('left',(parseInt(value)+6)+"px");
									$td.css('min-width',value+"px");
									selection.refreshBorders();
          						break;
          					case "bgcolor":
          						$td.css('background',value);
          						break;
          					case "indentLeft":
          						var pl = parseInt($td.css('padding-left').split('p')[0]);
          						if(pl-10>=0){
          							$td.css('padding-left',pl-10);
          						}
          						break;
          					case "indentRight":
          						var pl = parseInt($td.css('padding-left').split('p')[0]);
          						$td.css('padding-left',pl+10);
          						break;
          					case "category":
          						$td.parent().attr('name',value);
          						break;
          					case "border":
          						if(value=="top"){
          							if(priv.selStart.row==r){
              							var $td = $(grid.getCellAtCoords({row:r-1,col:c}));
              							$td.css('border-bottom-color','#000')
              						}
          						}else if(value=="bottom"){
          							if(priv.selEnd.row==r){
              							$td.css('border-bottom-color','#000')
              						}
          						}else if(value=="left"){
          							if(priv.selEnd.col==c){
          								var $td = $(grid.getCellAtCoords({row:r,col:c-1}));
              							$td.css('border-right-color','#000');
              						}
          						}else if(value=="right"){
          							if(priv.selEnd.col==c){
              							$td.css('border-right-color','#000')
              						}
          						}else if(value=="no"){
          							$td.css('border-color','#ccc')
              						if(priv.selStart.row==r){
              							var $td = $(grid.getCellAtCoords({row:r-1,col:c}));
              							$td.css('border-bottom-color','#ccc');
              							var $td = $(grid.getCellAtCoords({row:r,col:c-1}));
              							$td.css('border-right-color','#ccc');
              						}else if(priv.selStart.col==c){
              							var $td = $(grid.getCellAtCoords({row:r,col:c-1}));
              							$td.css('border-right-color','#ccc');
              						}
          						}else if(value=="all"){
          							$td.css('border-color','#000')
              						if(priv.selStart.row==r){
              							var $td = $(grid.getCellAtCoords({row:r-1,col:c}));
              							$td.css('border-bottom-color','#000');
              							var $td = $(grid.getCellAtCoords({row:r,col:c-1}));
              							$td.css('border-right-color','#000');
              						}else if(priv.selStart.col==c){
              							var $td = $(grid.getCellAtCoords({row:r,col:c-1}));
              							$td.css('border-right-color','#000');
              						}
          						}else if(value=="outSide"){
          							if(priv.selEnd.row==r){
              							$td.css('border-bottom-color','#000')
              						}
              						if(priv.selEnd.col==c){
              							$td.css('border-right-color','#000')
              						}
              						if(priv.selStart.row==r){
              							var $td = $(grid.getCellAtCoords({row:r-1,col:c}));
              							$td.css('border-bottom-color','#000');
              						}
              						if(priv.selStart.col==c){
              							var $td = $(grid.getCellAtCoords({row:r,col:c-1}));
              							$td.css('border-right-color','#000')
              						}
          						}
          						break;
         				}
    				}
    			}	
    		}
    	//TODO 重新解析table 存入cookie
    	
    	var XMLData = this.XML.parsingToXML();
    	$tableModel.val(XMLData);
    	parent.rtree.initModelTree();
    	return {start:{row:priv.selStart.row,col:priv.selStart.col},end:{row:priv.selEnd.row,col:priv.selEnd.col}};
    };
    Handsontable.XML= {
    	//XML
    	getTableXML:function(){
    		return $tableModel.val();
    	},
    	saveXML:function(){
    		var expFuntionData = Handsontable.expression.parsingExp();
    		var pathname = window.location.pathname;
    		var type = pathname.substring(pathname.length-5,pathname.length-4);
    		parent.layer.prompt({title:'请输入报表名称'},function(nm, index, elem){
		        if (nm!=null && nm!='')	{
		        	parent.layer.prompt({value:nm, title:'请输入简要的描述'},function(memo, indexMemo, elemMemo){
				    	if (memo != null && memo != "") {
			        		var uid=parent.guid();
				    		$.ajax({
				    			url : "../../saveDesignXML.action", 
				    			data:{
				    				expressionData:expFuntionData,
				                    parmsModel:$parmsModel.val(),
				                    parmsExtModel:$parmsExtModel.val(),
				    				dataSetsModel:$dataSetsModel.val(),
				    				fieldModel:$fieldModel.val(),
				    				tableXMLData:$tableModel.val(),
				    				uuid:uid,
				                    desc:nm,
				    				type:type,
				    				fileName:uid+".xml",
				    				reportVersion:"1.0",
				    				reportMemo:memo,
				    				mainUuid:uid
				    			},
				    			type:"post",
				    			success : function(data) {
				    				parent.layer.alert(data,function(){if(top.location.href.indexOf('?uuid=')!=-1){top.location.href=top.location.href.split('?uuid=')[0]+"?uuid="+uid;}else{top.location.href=top.location.href+"?uuid="+uid;}})
				    			}
				    		});
				    	}
				    	parent.layer.close(indexMemo);
		        	});
	    		}
		        parent.layer.close(index);
			});
    	},
    	updateXML:function(){
    		var expFuntionData = Handsontable.expression.parsingExp();
    		$.ajax({
    			url : "../../updateDesignXML.action", 
    			data:{
    				expressionData:expFuntionData,
                    parmsModel:$parmsModel.val(),
                    parmsExtModel:$parmsExtModel.val(),
    				tableXMLData:$tableModel.val(),
    				dataSetsModel:$dataSetsModel.val(),
    				fieldModel:$fieldModel.val(),
    				uuid:parent.GetQueryString("uuid")
    			},
    			type:"post",
    			success : function(data) {
    				parent.layer.alert(data)
    			}
    		});
    	},
    	openXML:function(){
    		var myuuid=parent.GetQueryString("uuid");
    		if(myuuid ==null || myuuid.toString().length<1) return;
    		var setData = [];
    		var tableStr,dataSetsStr;
			var xmlStrDoc = null;
			var expTmp = null;
			$.ajax({
    			url : "../../openDesignXML.action", 
    			type:"post",
    			data:{
    				uuid:myuuid
    			},
    			async:false,
    			success : function(data) {
    				if(data.xml!=undefined){
	    				$tableModel.val(data.xml.body);
	    				tableStr = data.xml.body;
	    				$dataSetsModel.val(data.xml.dataSets);
	    				$fieldModel.val(data.xml.fillreports);
	    				dataSetsStr= data.xml.dataSets;
	                    $parmsModel.val(data.xml.parmslist);
	                    $parmsExtModel.val(data.xml.parmsExtlist);
	    				expTmp = JSON.parse(data.xml.expression);
	    				parent.setProperties('orientation',data.xml.pageorder);
	    				parent.setProperties('paging',data.xml.pagesize);
	    				$('#rptTitle',parent.document).text(data.xml.title);
    				}else{
    					top.location.href='../../login.action';
    				}
    			}
    		});
			
			 Handsontable.dataFormat.initDic();
			 Handsontable.core = self;
			grid.clear();
			datamap.clear();
			var linkarr = new Array();
			var frameidarr = new Array();
			if (window.DOMParser) {
				var domParser = new DOMParser();
				//表格解析
				xmlStrDoc = domParser.parseFromString(tableStr, "text/xml");
				var rows,cols;
				rows = xmlStrDoc.childNodes[0].childNodes.length;
				cols = xmlStrDoc.childNodes[0].childNodes[0].childNodes.length;
				$('#demo').handsontable({
					rows: rows,
					cols: cols
				});
				var arrTh = $('#dataTable table thead').find('th');
				var trs = $('#rowHeaderDiv').find('tr');
				for(var r=0;r<xmlStrDoc.childNodes[0].childNodes.length;r++){
					var nodeRow = xmlStrDoc.childNodes[0].childNodes[r];
					var category = nodeRow.getAttribute("category");
					var isselect = nodeRow.getAttribute("isselect");
					var THheight = nodeRow.getAttribute("THheight");
					var visibility = nodeRow.getAttribute("visibility");
					THheight = parseInt(THheight.split('p')[0]);
					if(r==0){
						THheight = THheight;
					}else{
						if(THheight>22){
							THheight = THheight-1;
						}
					}
					if(visibility!=null){
//						$($(trs[r+1]).find('th')).attr('name','visibility').css('background-color','#E8776C');
					}
					$(trs[r+1]).css('height',THheight);
					$(trs[r+1]).find('th').css('height',THheight);
					$(trs[r+1]).find('div').css('top',THheight-5);
					for(var c=0;c<nodeRow.childNodes.length;c++){
						var nodeCol = nodeRow.childNodes[c];
						var expText = nodeCol.getAttribute("expText");
						var dic = nodeCol.getAttribute("dic");
						var fieldFormat = nodeCol.getAttribute("fieldFormat");
						var format = nodeCol.getAttribute("format");
						var link = nodeCol.getAttribute("link");
						var frameid = nodeCol.getAttribute("frameid");
						var expDa = nodeCol.getAttribute("expData");
						var expType = nodeCol.getAttribute("expType");
						var cvisibility = nodeCol.getAttribute("visibility");
						var rowspan = nodeCol.getAttribute("rowspan");
						var colspan = nodeCol.getAttribute("colspan");
						var ishidden = nodeCol.getAttribute("ishidden");
						var coords = nodeCol.getAttribute("coords");
						var nodeText = nodeCol.textContent;
						//var nodeText = nodeCol.innerHTML;
						var THwidth = nodeCol.getAttribute("THwidth");
						var width = nodeCol.getAttribute("width");
						var height = nodeCol.getAttribute("height");
						var fontFamily = nodeCol.getAttribute("font-family");
						var fontSize = nodeCol.getAttribute("font-size");
						var fontColor = nodeCol.getAttribute("font-color");
						var fontWeight = nodeCol.getAttribute("font-weight");
						var fontStyle = nodeCol.getAttribute("font-style");
						var textDecoration = nodeCol.getAttribute("text-decoration");
						var align = nodeCol.getAttribute("text-align");
						var bgcolor = nodeCol.getAttribute("background-color");
						var border = nodeCol.getAttribute("border");
						var wordWrap = nodeCol.getAttribute("white-space");
						var valign = nodeCol.getAttribute("vertical-align");
						var relatedid=nodeCol.getAttribute("relatedid");
						border = eval("("+border+")");
						var $td = $(grid.getCellAtCoords({row:r,col:c}));
						if(rowspan||colspan){
							$td.attr('rowspan',rowspan).attr('colspan',colspan);
						}else if(ishidden){
							$td.attr('name',coords).hide();
						}
//						alert(border.rightColor)
//						alert(width+","+height+","+fontFamily+","+fontSize+","+fontColor+","+fontWeight+","+fontStyle+","+textDecoration+","+align+","+bgcolor)
						var ths = $('#colHeaderDiv').find('th');
						if(cvisibility!=null){
//							$(ths[c+1]).attr('name','visibility').css('background-color','#E8776C');
						}
						$(ths[c+1]).css('min-width',THwidth);
						$(ths[c+1]).find('div').css('left',parseInt(THwidth.split('p')[0])+5);
						$td.parent().attr('name',category);
						$td.parent().attr('isselect',isselect);
						$(arrTh[c+1]).css('width',width);
						$td.parent().css('height',height);
						$td.css('font-family',fontFamily);
						$td.css('font-size',fontSize);
						$td.css('color',fontColor);
						$td.css('font-weight',fontWeight);
						$td.css('font-style',fontStyle);
						$td.css('text-decoration',textDecoration.split(' ')[0]);
						$td.css('text-align',align);
						$td.css('vertical-align',valign);
						$td.css('background-color',bgcolor);
						$td.css('border-left-color',border.leftColor);
						$td.css('border-top-color',border.topColor);
						$td.css('border-right-color',border.rightColor);
						$td.css('border-bottom-color',border.bottomColor);
						$td.css('white-space',wordWrap);
						$td.attr('expData',expDa);
						$td.attr('expType',expType);
						$td.attr('link',link);
						$td.attr('frameid',frameid);
						$td.attr('format',format);
						$td.attr('fieldFormat',fieldFormat);
						$td.attr('dic',dic);
						$td.attr('expText',expText);
						$td.attr('relatedid',relatedid);
						$td.html(nodeText);
						setData.push([r, c, nodeText, undefined]);
						if(link!=""&&link!=null){
							linkarr.push($td);
						}
						if(frameid!=""&&frameid!=null){
							frameidarr.push($td);
						}
						
					}
				}
				//数据集解析
				parent.rprop.parseDataSets(dataSetsStr);
			} else {
//				xmlStrDoc = new ActiveXObject("Microsoft.XMLDOM");
//				xmlStrDoc.async = "false";
//				xmlStrDoc.loadXML(str);
				parent.layer.alert("您的浏览器找不到所需的XML解析器。")
			}
			self.setDataAtCell(setData, null, null, null, "loadData" || 'populateFromArray');
			selection.deselect();
			parent.rtree.initModelTree();
			
		
			for(var l=0;l<linkarr.length;l++){
				var strt = linkarr[l].html();
				linkarr[l].html("");
        		linkarr[l].append("<a href='javascript:void(0);'>"+strt+"<a>");
			}
			for(var l=0;l<frameidarr.length;l++){
				var strt = frameidarr[l].html();
				frameidarr[l].html("");
				frameidarr[l].append(strt);
			}
			
			for(var k in expTmp){
				for(var n in expTmp[k]){
					for(var i=0;i<expTmp[k][n].length;i++){
						var arr = expTmp[k][n][i].split(',');
						var td = grid.getCellAtCoords({row:parseInt(arr[0]),col:parseInt(arr[1])});
						expTmp[k][n][i] = td;
					}
					$(grid.getCellAtCoords({row:parseInt(n.split(',')[0]),col:parseInt(n.split(',')[1])})).append('<div class="exp_flag"></div>');
				}
			}
			sumData = expTmp.sum;
			averageData = expTmp.average;
			countData = expTmp.count;
			discountData = expTmp.discount;
			maxData = expTmp.max;
			minData = expTmp.min;
    	},
    	importExcel:function(){//导入excel模板
    		var setData = [];
    		var tableStr,dataSetsStr;
			var xmlStrDoc = null;
			var expTmp = null;
			
			tableStr = $tableModel.val();
			 Handsontable.core = self;
			 grid.clear();
				datamap.clear();
			var linkarr = new Array();
			var frameidarr = new Array();
			if (window.DOMParser) {
				var domParser = new DOMParser();
				//表格解析
				xmlStrDoc = domParser.parseFromString(tableStr, "text/xml");
				var rows,cols;
				rows = xmlStrDoc.childNodes[0].childNodes.length;
				cols = xmlStrDoc.childNodes[0].childNodes[0].childNodes.length;
				$('#demo').handsontable({
					rows: rows,
					cols: cols
				});
				var arrTh = $('#dataTable table thead').find('th');
				var trs = $('#rowHeaderDiv').find('tr');
				for(var r=0;r<xmlStrDoc.childNodes[0].childNodes.length;r++){
					var nodeRow = xmlStrDoc.childNodes[0].childNodes[r];
					var category = nodeRow.getAttribute("category");
					var isselect = nodeRow.getAttribute("isselect");
					var THheight = nodeRow.getAttribute("THheight");
					var visibility = nodeRow.getAttribute("visibility");
					THheight = parseInt(THheight.split('p')[0]);
					if(r==0){
						THheight = THheight;
					}else{
						if(THheight>22){
							THheight = THheight-1;
						}
					}
					if(visibility!=null){
//						$($(trs[r+1]).find('th')).attr('name','visibility').css('background-color','#E8776C');
					}
					$(trs[r+1]).css('height',THheight);
					$(trs[r+1]).find('th').css('height',THheight);
					$(trs[r+1]).find('div').css('top',THheight-5);
					for(var c=0;c<nodeRow.childNodes.length;c++){
						var nodeCol = nodeRow.childNodes[c];
						var expText = nodeCol.getAttribute("expText");
						var dic = nodeCol.getAttribute("dic");
						var fieldFormat = nodeCol.getAttribute("fieldFormat");
						var format = nodeCol.getAttribute("format");
						var link = nodeCol.getAttribute("link");
						var frameid = nodeCol.getAttribute("frameid");
						var expDa = nodeCol.getAttribute("expData");
						var expType = nodeCol.getAttribute("expType");
						var rowspan = nodeCol.getAttribute("rowspan");
						var colspan = nodeCol.getAttribute("colspan");
						var ishidden = nodeCol.getAttribute("ishidden");
						var coords = nodeCol.getAttribute("coords");
						var nodeText = nodeCol.textContent;
						var THwidth = nodeCol.getAttribute("THwidth");
						var width = nodeCol.getAttribute("width");
						var height = nodeCol.getAttribute("height");
						var fontFamily = nodeCol.getAttribute("font-family");
						var fontSize = nodeCol.getAttribute("font-size");
						var fontColor = nodeCol.getAttribute("font-color");
						var fontWeight = nodeCol.getAttribute("font-weight");
						var fontStyle = nodeCol.getAttribute("font-style");
						var textDecoration = nodeCol.getAttribute("text-decoration");
						var align = nodeCol.getAttribute("text-align");
						var bgcolor = nodeCol.getAttribute("background-color");
						var border = nodeCol.getAttribute("border");
						var wordWrap = nodeCol.getAttribute("white-space");
						var valign = nodeCol.getAttribute("vertical-align");
						var offsetLeft=nodeCol.getAttribute("offsetLeft");
						var offsetTop=nodeCol.getAttribute("offsetTop");
						border = eval("("+border+")");
						
						var $td = $(grid.getCellAtCoords({row:r,col:c}));
						if(rowspan||colspan){
							$td.attr('rowspan',rowspan).attr('colspan',colspan);
						}else if(ishidden){
							$td.attr('name',coords).hide();
						}
						var ths = $('#colHeaderDiv').find('th');
//						if(cvisibility!=null){
////							$(ths[c+1]).attr('name','visibility').css('background-color','#E8776C');
//						}
						
						$(ths[c+1]).css('min-width',THwidth);
						$(ths[c+1]).find('div').css('left',parseInt(THwidth.split('p')[0])+5);
						$td.parent().attr('name',category);
						$td.parent().attr('isselect',isselect);
						$(arrTh[c+1]).css('width',width);
						$td.parent().css('height',height);
						$td.css('font-family',fontFamily);
						$td.css('font-size',fontSize);
						$td.css('color',fontColor);
						$td.css('font-weight',fontWeight);
						$td.css('font-style',fontStyle);
						$td.css('text-decoration',textDecoration.split(' ')[0]);
						$td.css('text-align',align);
						$td.css('vertical-align',valign);
						$td.css('background-color',bgcolor);
						$td.css('border-left-color',border.leftColor);
						$td.css('border-top-color',border.topColor);
						$td.css('border-right-color',border.rightColor);
						$td.css('border-bottom-color',border.bottomColor);
						$td.css('white-space',wordWrap);
						$td.attr('expData',expDa);
						$td.attr('expType',expType);
						$td.attr('link',link);
						$td.attr('frameid',frameid);
						$td.attr('format',format);
						$td.attr('fieldFormat',fieldFormat);
						$td.attr('dic',dic);
						$td.attr('expText',expText);
						$td.attr('offsetLeft',offsetLeft);
						$td.attr('offsetTop',offsetTop);
						//$td.attr('colwidth',$(td).outerWidth());
						$td.html(nodeText);
						setData.push([r, c, nodeText, undefined]);
					}
				}
			} else {
//				xmlStrDoc = new ActiveXObject("Microsoft.XMLDOM");
//				xmlStrDoc.async = "false";
//				xmlStrDoc.loadXML(str);
				parent.layer.alert("您的浏览器找不到所需的XML解析器。")
			}
			self.setDataAtCell(setData, null, null, null, "loadData" || 'populateFromArray');
			selection.deselect();
			parent.rtree.initModelTree();
    	},
    	parsingToXML:function(){
			var arr = grid.getAllCells();
			var lastTd = grid.getCellCoords(arr[arr.length-1]);
			var XMLStr="<body";
			var tb_orientation = $('#tb_orientation',parent.document).val();
			var tb_paging = $('#tb_paging',parent.document).val();
			XMLStr+=" pageorder='"+tb_orientation+"'";
			XMLStr+=" pagesize='"+tb_paging+"'";
			XMLStr+=">";
			var row=0;
			var flag = true;
			var attribute = "";
			for(var i=0;i<arr.length;i++){
				var offsetTop,offsetLeft,visibility,category,height,colwidth,width,THwidth,THheight,fontStyle,textDecoration,fontWeight,fontSize,fontFamily,fontColor,bgcolor,align,border="";
				var td= arr[i];
				if(grid.getCellCoords(td).row>row){
					row++;
					flag = true;
					XMLStr += "</row>"
				}
				if(flag){
					var trs = $('#rowHeaderDiv').find('tr');
					
					THheight = parseInt($(trs[grid.getCellCoords(td).row+1]).css('height').split('p')[0]);
					if(grid.getCellCoords(td).row==0){
						THheight=THheight-2;
					}
					visibility = $($(trs[grid.getCellCoords(td).row+1]).find('th')).attr('name');
					var tmp="";
					if(visibility!=null&&visibility!=""){
						tmp = " visibility ='visibility'";
					}
					var cv = $(td).parent().attr('name');
					if(typeof(cv)=="undefined"||cv=="undefined"||cv==""){
						cv = "dataarea";
					}
					var isselect=$(td).parent().attr('isselect')==undefined?$(td).parent().prop('isselect'):$(td).parent().attr('isselect');
					if(!isselect) isselect="";
					XMLStr += "<row category='"+cv+"' isselect='"+isselect+"' THheight='"+THheight+"px' "+tmp+">";
					flag = false;
				}
				if($(td).attr("rowspan")||$(td).attr("colspan")){
					attribute = " rowspan='"+$(td).attr("rowspan")+"' colspan='"+$(td).attr("colspan")+"'";
				}else if($(td).is(":hidden")){
					attribute = " ishidden='true' coords='"+$(td).attr('name')+"'";
				}else{
					attribute = "";
				}
				var ths = $('#colHeaderDiv').find('th');
				THwidth = $(ths[grid.getCellCoords(td).col+1]).css('min-width');
				var rvisibility = $(ths[grid.getCellCoords(td).col+1]).attr('name');
				var v=""
//					
				if(rvisibility&&rvisibility!="null"&&rvisibility!=""){
					v = " visibility='visibility'";
				}
//				var temp=0;
//				if($(td).attr("colspan")>1){
////					temp = 4.5*parseInt($(td).attr("colspan"));
////					alert($(td).outerWidth())
//				}
				var expDa=$(td).attr('expData')==undefined?$(td).prop('expData'):$(td).attr('expData');
				if(!expDa) expDa="";
				var expType=$(td).attr('expType')==undefined?$(td).prop('expType'):$(td).attr('expType');
				if(!expType) expType="";
				var link=$(td).attr('link')==undefined?$(td).prop('link'):$(td).attr('link');
				if(!link) link="";
				var frameid=$(td).attr('frameid')==undefined?$(td).prop('frameid'):$(td).attr('frameid');
				if(!frameid) frameid="";
				var relatedid=$(td).attr('relatedid')==undefined?$(td).prop('relatedid'):$(td).attr('relatedid');
				if(!relatedid) relatedid="";
				var format=$(td).attr('format')==undefined?$(td).prop('format'):$(td).attr('format');
				if(!format) format="";
				var fieldFormat=$(td).attr('fieldFormat')==undefined?$(td).prop('fieldFormat'):$(td).attr('fieldFormat');
				if(!fieldFormat) fieldFormat="";
				var dic=$(td).attr('dic')==undefined?$(td).prop('dic'):$(td).attr('dic');
				if(!dic) dic="";
				var expText=$(td).attr('expText')==undefined?$(td).prop('expText'):$(td).attr('expText');
				if(!expText){expText="";}else{expText=expText.replace(/\>/g, "&gt;").replace(/\</g, "&lt;").trim();}
				
				var offleft = $(td).attr('offsetLeft')==undefined?$(td).prop('offsetLeft'):$(td).attr('offsetLeft');
				if(!offleft) offleft="";
				var offtop = $(td).attr('offsetTop')==undefined?$(td).prop('offsetTop'):$(td).attr('offsetTop');
				if(!offtop) offtop="";
				
				expText = " expText='"+expText+"'";
				dic = " dic='"+dic+"'";
				fieldFormat = " fieldFormat='"+fieldFormat+"'";
				format = " format='"+format+"'";
				expDa = " expData='"+expDa+"'";
				expType = " expType='"+expType+"'";
				var tdOffset = $(td).offset();
				offsetTop = " offsetTop='"+offtop+"'";
		        offsetLeft = " offsetLeft='"+offleft+"'";
				colwidth = " colwidth='"+$(td).outerWidth()+"px'";
				width = " width='"+THwidth+"'";
				THwidth = " THwidth='"+THwidth+"'";
          		height = " height='"+$(td).parent().css('height')+"'";
				fontFamily = " font-family='"+$(td).css('font-family')+"'";
         		fontSize = " font-size='"+$(td).css('font-size')+"'";
          		fontColor = " font-color='"+$(td).css('color')+"'";
          		fontWeight = " font-weight='"+$(td).css('font-weight')+"'";
          		fontStyle = " font-style='"+$(td).css('font-style')+"'";
          		textDecoration = " text-decoration='"+$(td).css('text-decoration')+"'";
          		align = " text-align='"+$(td).css('text-align')+"'";
          		bgcolor = " background-color='"+$(td).css('background-color')+"'";
          		border = " border='{leftColor:\""+$(td).css('border-left-color')+"\",topColor:\""+$(td).css('border-top-color')+"\",rightColor:\""+$(td).css('border-right-color')+"\",bottomColor:\""+$(td).css('border-bottom-color')+"\"}'";
				var wordWrap = " white-space='"+$(td).css('white-space')+"'";
				var valign = " vertical-align='"+$(td).css('vertical-align')+"'";
				var vhtml="";
				if(link==""){
					vhtml = $(td).html()
				}else{
					vhtml = $($(td).find('a')[0]).html();
				}
				link = " link='"+link+"'";
				if(frameid!=""){
					vhtml = $(td).html();
				}
				frameid = " frameid='"+frameid+"'";
				relatedid = " relatedid='"+relatedid+"'";
          		XMLStr +="<col"+expText+dic+format+fieldFormat+expType+expDa+link+frameid+relatedid+v+attribute+offsetTop+offsetLeft+colwidth+THwidth+width+height+fontFamily+fontSize+fontColor+fontWeight+fontStyle+textDecoration+valign+align+bgcolor+border+wordWrap+">"+vhtml+"</col>";
			}
			XMLStr +="</row></body>";
			return XMLStr;
    	},
        getCellProperties:function(coords){
        	var XMLData =$tableModel.val();
        	if(XMLData=="") return;
//        	if(selection.isMultiple()) return;
        	var row,col;
    		if(coords==null){
    			row = priv.selStart.row;
        		col = priv.selStart.col;
    		}else{
    			row = coords.start.row;
    			col = coords.start.col;
    		}
        	var xmlStrDoc = null;
        	var jsonp = null;
        	if (window.DOMParser) {
    			var domParser = new DOMParser();
    			xmlStrDoc = domParser.parseFromString(XMLData, "text/xml");
    			var rownode = xmlStrDoc.childNodes[0].childNodes[row];
    			if(!rownode){
    				return null;
    			}
    			
    			var category = rownode.getAttribute("category");
    			var isselect = rownode.getAttribute("isselect");
    			var THheight = rownode.getAttribute("THheight");
    			var node = xmlStrDoc.childNodes[0].childNodes[row].childNodes[col];
    			var nodeText = node.textContent;
    			var THwidth = node.getAttribute("THwidth");
				var width = node.getAttribute("width");
				var height = node.getAttribute("height");
				var fontFamily = node.getAttribute("font-family");
				var fontSize = node.getAttribute("font-size");
				var fontColor = node.getAttribute("font-color");
				var fontWeight = node.getAttribute("font-weight");
				var fontStyle = node.getAttribute("font-style");
				var textDecoration = node.getAttribute("text-decoration");
				var align = node.getAttribute("text-align");
				var bgcolor = node.getAttribute("background-color");
				var wordWrap = node.getAttribute("white-space");
				var valign = node.getAttribute("vertical-align");
				var fieldFormat = node.getAttribute("fieldFormat");
				var dic = node.getAttribute("dic");
				var expr = node.getAttribute("expText");
//				var border = {
//						top:false,
//						bottom:false,
//						left:false,
//						right:false
//				};
//				if(eval("("+xmlStrDoc.childNodes[0].childNodes[row-1].childNodes[col].getAttribute("border")+")").bottomColor!=rgb(204, 204, 204)){
//					border.top=true;
//				}
//				if(eval("("+node.getAttribute("border")+")").bottomColor!=rgb(204, 204, 204)){
//					border.bottom=true;
//				}
//				if(eval("("+xmlStrDoc.childNodes[0].childNodes[row].childNodes[col-1].getAttribute("border")+")").rightColor!=rgb(204, 204, 204)){
//					border.left=true;
//				}
//				if(eval("("+node.getAttribute("border")+")").rightColor!=rgb(204, 204, 204)){
//					border.right=true;
//				}
				jsonp = {
						text:nodeText,
						isselect:(isselect==1)?1:0,
						category:category,
						THheight:THheight.split('p')[0],
						THwidth:THwidth.split('p')[0],
						width:width.split('p')[0],
						height:height.split('p')[0],
						fontFamily:fontFamily,
						fontSize:fontSize.split('p')[0],
						fontColor:fontColor,
						fontWeight:(fontWeight=="400"||fontWeight=="normal")?0:1,
						fontStyle:(fontStyle=="normal")?0:1,
						wordWrap:(wordWrap=="nowrap"||wordWrap==""||wordWrap==null)?0:1,
						textDecoration:textDecoration.split(' ')[0],
						align:(align=="start")?"left":align,
						valign:valign,
						bgcolor:bgcolor,
						format:fieldFormat,
						dic:dic,
						expr:expr
//						border:border
				};
    		}else {
    			parent.layer.alert("您的浏览器找不到所需的XML解析器。")
    		}
        	return jsonp;
        }
    }
    //TODO 函数
    Handsontable.expression = {
    		creExpFun : function creExpFun(text){
    			var r = priv.selStart.row;
        		var c = priv.selStart.col;
        		selection.deselect();
        		if(r==null||c==null) return;
    			var $td = $(grid.getCellAtCoords({row: r,col: c}));
    			var tdOffset = $td.offset();
    			var $div = $('<div></div>');
    			$div.attr("class", "expFun");
    			$div.css('top',tdOffset.top)
    			.css('left',tdOffset.left);
    			
    			var $div2 = $('<div></div>');
    			$div2.css('height','auto')
    			.css('width','auto')
    			.css('min-width','80px')
    			.css('background','none repeat scroll 0 0 transparent')
    			.css('padding','2px')
    			.css('text-overflow','ellipsis')
    			.css('white-space','nowrap')
    			.css('display','inline')
    			.attr('id','ExpFunDiv')
    			.html(text);
    			
    			
    			var $saved = $('<div></div>');
    			$saved.css('cursor','pointer')
    			.css('height','16px')
    			.css('position','absolute')
    			.css('top','2px')
    			.css('width','16px')
    			.css('right','-25px')
    			.css('background','url("../../images/des/saved.png") no-repeat scroll center center transparent');
    			$saved.bind('click',function(){
    				if($('#ExpFunDiv').html().indexOf("SUM") > 0 ){
    					var sum=0;
    					sumData[priv.selStart.row+","+priv.selStart.col] = new Array();
    					var arr = sumData[priv.selStart.row+","+priv.selStart.col]
    					for(var i=0;i<expData.sum.length;i++){
    						var expDa=$(expData.average[i]).attr('expData')==undefined?$(expData.average[i]).prop('expData'):$(expData.average[i]).attr('expData');
    						if(!expDa) expDa ="";
    						else expDa +="#";
    						$(expData.sum[i]).attr('expData',expDa+"sum,"+priv.selStart.row+","+priv.selStart.col);
    						arr[arr.length] = expData.sum[i];
    						$($(expData.sum[i]).find('div')[0]).remove();
        					var tmp = parseInt($(expData.sum[i]).html());
        					if(!isNaN(tmp)){
        						sum +=tmp;
        					}
        				}
    					var td = grid.getCellAtCoords(priv.selStart);
    					$(td).html(sum);
    					expData.sum = new Array();
    					$(td).attr('expType','sum');
    					$(td).append('<div class="exp_flag"></div>')
    				}else if($('#ExpFunDiv').html().indexOf("AVERAGE") > 0 ){
    					var average=0;
    					var count=0;
    					averageData[priv.selStart.row+","+priv.selStart.col] = new Array();
    					var arr = averageData[priv.selStart.row+","+priv.selStart.col]
    					for(var i=0;i<expData.average.length;i++){
    						var expDa=$(expData.average[i]).attr('expData')==undefined?$(expData.average[i]).prop('expData'):$(expData.average[i]).attr('expData');
    						if(!expDa) expDa="";
    						else expDa +="#";
    						$(expData.average[i]).attr('expData',expDa+"average,"+priv.selStart.row+","+priv.selStart.col);
    						arr[arr.length] = expData.average[i];
    						$($(expData.average[i]).find('div')[0]).remove();
        					var tmp = parseInt($(expData.average[i]).html());
        					if(!isNaN(tmp)){
        						average +=tmp;
        						count++;
        					}
        				}
    					average = average/count;
    					var td = grid.getCellAtCoords(priv.selStart);
    					$(td).html(average);
    					expData.average = new Array();
    					$(td).attr('expType','average');
    					$(td).append('<div class="exp_flag"></div>')
    				}else if($('#ExpFunDiv').html().indexOf("COUNT") > 0 ){
    					var count=0;
    					countData[priv.selStart.row+","+priv.selStart.col] = new Array();
    					var arr = countData[priv.selStart.row+","+priv.selStart.col]
    					for(var i=0;i<expData.count.length;i++){
    						var expDa=$(expData.count[i]).attr('expData')==undefined?$(expData.count[i]).prop('expData'):$(expData.count[i]).attr('expData');
    						if(!expDa) expDa="";
    						else expDa +="#";
    						$(expData.count[i]).attr('expData',expDa+"count,"+priv.selStart.row+","+priv.selStart.col);
    						arr[arr.length] = expData.count[i];
    						$($(expData.count[i]).find('div')[0]).remove();
        					if($(expData.count[i]).html()!=""){
        						count++;
        					}
        				}
    					var td = grid.getCellAtCoords(priv.selStart);
    					$(td).html(count);
    					expData.count = new Array();
    					$(td).attr('expType','count');
    					$(td).append('<div class="exp_flag"></div>');
    				}else if($('#ExpFunDiv').html().indexOf("DISCOUNT") > 0 ){
    					var discount=0;
    					discountData[priv.selStart.row+","+priv.selStart.col] = new Array();
    					var arr = discountData[priv.selStart.row+","+priv.selStart.col]
    					for(var i=0;i<expData.discount.length;i++){
    						var expDa=$(expData.discount[i]).attr('expData')==undefined?$(expData.discount[i]).prop('expData'):$(expData.discount[i]).attr('expData');
    						if(!expDa) expDa="";
    						else expDa +="#";
    						$(expData.discount[i]).attr('expData',expDa+"discount,"+priv.selStart.row+","+priv.selStart.col);
    						arr[arr.length] = expData.discount[i];
    						$($(expData.discount[i]).find('div')[0]).remove();
        					if($(expData.discount[i]).html()!=""){
        						discount++;
        					}
        				}
    					var td = grid.getCellAtCoords(priv.selStart);
    					$(td).html(discount);
    					expData.discount = new Array();
    					$(td).attr('expType','discount');
    					$(td).append('<div class="exp_flag"></div>')
    				}else if($('#ExpFunDiv').html().indexOf("MAX") > 0 ){
    					var max=0;
    					maxData[priv.selStart.row+","+priv.selStart.col] = new Array();
    					var arr = maxData[priv.selStart.row+","+priv.selStart.col]
    					for(var i=0;i<expData.max.length;i++){
    						var expDa=$(expData.max[i]).attr('expData')==undefined?$(expData.max[i]).prop('expData'):$(expData.max[i]).attr('expData');
    						if(!expDa) expDa="";
    						else expDa +="#";
    						$(expData.max[i]).attr('expData',expDa+"max,"+priv.selStart.row+","+priv.selStart.col);
    						arr[arr.length] = expData.max[i];
    						$($(expData.max[i]).find('div')[0]).remove();
    						var tmp = parseInt($(expData.max[i]).html());
        					if(!isNaN(tmp)){
        						if(tmp>max){
        							max = tmp;
        						}
        					}
        				}
    					var td = grid.getCellAtCoords(priv.selStart);
    					$(td).html(max);
    					expData.max = new Array();
    					$(td).attr('expType','max');
    					$(td).append('<div class="exp_flag"></div>')
    				}else if($('#ExpFunDiv').html().indexOf("MIN") > 0 ){
    					var min=0;
    					minData[priv.selStart.row+","+priv.selStart.col] = new Array();
    					var arr = minData[priv.selStart.row+","+priv.selStart.col]
    					for(var i=0;i<expData.min.length;i++){
    						var expDa=$(expData.min[i]).attr('expData')==undefined?$(expData.min[i]).prop('expData'):$(expData.min[i]).attr('expData');
    						if(!expDa) expDa="";
    						else expDa +="#";
    						$(expData.min[i]).attr('expData',expDa+"min,"+priv.selStart.row+","+priv.selStart.col);
    						arr[arr.length] = expData.min[i];
    						$($(expData.min[i]).find('div')[0]).remove();
    						var tmp = parseInt($(expData.min[i]).html());
        					if(!isNaN(tmp)){
        						if(i==0){
          						  min = tmp; 
          					  }else if(tmp<min){
          						  min=tmp;
          					  }
        					}
        				}
    					var td = grid.getCellAtCoords(priv.selStart);
    					$(td).html(min);
    					expData.min = new Array();
    					$(td).attr('expType','min');
    					$(td).append('<div class="exp_flag"></div>')
    				}
    				$('#ExpFunDiv').parent().remove();
    				var XMLData = Handsontable.XML.parsingToXML();
    		  		$tableModel.val(XMLData);
    			});
    			
    			var $cross = $('<div></div>');
    			$cross.css('cursor','pointer')
    			.css('height','16px')
    			.css('position','absolute')
    			.css('top','2px')
    			.css('width','16px')
    			.css('right','-45px')
    			.css('background','url("../../images/des/cross.png") no-repeat scroll center center transparent');
    			$cross.bind('click',function(){
    				if($('#ExpFunDiv').html().indexOf("SUM") > 0 ){
    					for(var i=0;i<expData.sum.length;i++){
        					$($(expData.sum[i]).find('div')[0]).remove();
        				}
        				$('#ExpFunDiv').parent().remove();
        				expData.sum = new Array();
    				}else if($('#ExpFunDiv').html().indexOf("AVERAGE") > 0 ){
    					for(var i=0;i<expData.average.length;i++){
        					$($(expData.average[i]).find('div')[0]).remove();
        				}
        				$('#ExpFunDiv').parent().remove();
        				expData.average = new Array();
    				}else if($('#ExpFunDiv').html().indexOf("COUNT") > 0 ){
    					for(var i=0;i<expData.count.length;i++){
        					$($(expData.count[i]).find('div')[0]).remove();
        				}
        				$('#ExpFunDiv').parent().remove();
        				expData.count = new Array();
    				}else if($('#ExpFunDiv').html().indexOf("DISCOUNT") > 0 ){
    					for(var i=0;i<expData.discount.length;i++){
        					$($(expData.discount[i]).find('div')[0]).remove();
        				}
        				$('#ExpFunDiv').parent().remove();
        				expData.discount = new Array();
    				}else if($('#ExpFunDiv').html().indexOf("MAX") > 0 ){
    					for(var i=0;i<expData.max.length;i++){
        					$($(expData.max[i]).find('div')[0]).remove();
        				}
        				$('#ExpFunDiv').parent().remove();
        				expData.max = new Array();
    				}else if($('#ExpFunDiv').html().indexOf("MIN") > 0 ){
    					for(var i=0;i<expData.min.length;i++){
        					$($(expData.min[i]).find('div')[0]).remove();
        				}
        				$('#ExpFunDiv').parent().remove();
        				expData.min = new Array();
    				}
    			});
    			
    			$div.append($div2);
    			$div.append($saved);
    			$div.append($cross);
    			$('#demo').append($div);
    		},
    		clearExpFun:function(){
    			var r = priv.selStart.row;
        		var c = priv.selStart.col;
        		if(r==null||c==null) return;
        		var $td = $(grid.getCellAtCoords({row: r,col: c}));
        		$td.html("");
        		var expType=$td.attr('expType')==undefined?$td.prop('expType'):$td.attr('expType');
       		  	if(expType=="sum"){
       		  			for(var z=0;z<sumData[r+","+c].length;z++){
       		  				$($(sumData[r+","+c][z]).find('div')[0]).remove();
       		  			}
       		  		delete sumData[r+","+c]; 
       		 	$($td.find('div')[0]).remove();
       		 	$td.attr('expType',null);
       		  	}else if(expType=="average"){
   		  			for(var z=0;z<averageData[r+","+c].length;z++){
   		  				$($(averageData[r+","+c][z]).find('div')[0]).remove();
   		  			}
   		  			delete averageData[r+","+c]; 
   		  		$($td.find('div')[0]).remove();
   		  		$td.attr('expType',null);
       		  	}else if(expType=="count"){
   		  			for(var z=0;z<countData[r+","+c].length;z++){
   		  				$($(countData[r+","+c][z]).find('div')[0]).remove();
   		  			}
   		  			delete countData[r+","+c]; 
   		  		$($td.find('div')[0]).remove();
   		  		$td.attr('expType',null);
       		  	}else if(expType=="discount"){
   		  			for(var z=0;z<discountData[r+","+c].length;z++){
   		  				$($(discountData[r+","+c][z]).find('div')[0]).remove();
   		  			}
   		  			delete discountData[r+","+c]; 
   		  		$($td.find('div')[0]).remove();
   		  		$td.attr('expType',null);
       		  	}else if(expType=="max"){
   		  			for(var z=0;z<maxData[r+","+c].length;z++){
   		  				$($(maxData[r+","+c][z]).find('div')[0]).remove();
   		  			}
   		  			delete maxData[r+","+c];  
   		  		$($td.find('div')[0]).remove();
   		  		$td.attr('expType',null);
       		  	}else if(expType=="min"){
   		  			for(var z=0;z<minData[r+","+c].length;z++){
   		  				$($(minData[r+","+c][z]).find('div')[0]).remove();
   		  			}
   		  			delete minData[r+","+c];  
   		  		$($td.find('div')[0]).remove();
   		  		$td.attr('expType',null);
       		  	}
    		},
    		parsingExp:function(){
    			var expFuntionData={
        				sum:{},
        				average:{},
        				count:{},
        				discount:{},
        				max:{},
        				min:{}
        		};
        		for(var k in sumData){
        			expFuntionData.sum[k]=new Array();
        			for(var i=0;i<sumData[k].length;i++){
        				var arr = expFuntionData.sum[k];
        				var coords = grid.getCellCoords(sumData[k][i]);
        				arr[arr.length] = coords.row+","+coords.col;
        			}
        		}
        		for(var k in averageData){
        			expFuntionData.average[k]=new Array();
        			for(var i=0;i<averageData[k].length;i++){
        				var arr = expFuntionData.average[k];
        				var coords = grid.getCellCoords(averageData[k][i]);
        				arr[arr.length] = coords.row+","+coords.col;
        			}
        		}
        		for(var k in countData){
        			expFuntionData.count[k]=new Array();
        			for(var i=0;i<countData[k].length;i++){
        				var arr = expFuntionData.count[k];
        				var coords = grid.getCellCoords(countData[k][i]);
        				arr[arr.length] = coords.row+","+coords.col;
        			}
        		}
        		for(var k in discountData){
        			expFuntionData.discount[k]=new Array();
        			for(var i=0;i<discountData[k].length;i++){
        				var arr = expFuntionData.discount[k];
        				var coords = grid.getCellCoords(discountData[k][i]);
        				arr[arr.length] = coords.row+","+coords.col;
        			}
        		}
        		for(var k in maxData){
        			expFuntionData.max[k]=new Array();
        			for(var i=0;i<maxData[k].length;i++){
        				var arr = expFuntionData.max[k];
        				var coords = grid.getCellCoords(maxData[k][i]);
        				arr[arr.length] = coords.row+","+coords.col;
        			}
        		}
        		for(var k in minData){
        			expFuntionData.min[k]=new Array();
        			for(var i=0;i<minData[k].length;i++){
        				var arr = expFuntionData.min[k];
        				var coords = grid.getCellCoords(minData[k][i]);
        				arr[arr.length] = coords.row+","+coords.col;
        			}
        		}
        		return JSON.stringify(expFuntionData);
    		},
    		setHyperlink:function(str){
    			var r = priv.selStart.row;
        		var c = priv.selStart.col;
        		var $td = $(grid.getCellAtCoords({row: r,col: c}));
        		var vhtml="";
        		var link=$td.attr('link')==undefined?$td.prop('link'):$td.attr('link');
				if(link){
					vhtml = $($td.find('a')[0]).html();
				}else{
					vhtml = $td.html();
				}
        		$td.html("");
        		$td.append("<a href='javascript:void(0);'>"+vhtml+"</a>");
        		$td.attr('link',str);
        		var XMLData = Handsontable.XML.parsingToXML();
		  		$tableModel.val(XMLData);
    		},
    		getHyperlink:function(){
    			var r = priv.selStart.row;
        		var c = priv.selStart.col;
        		var $td = $(grid.getCellAtCoords({row: r,col: c}));
        		var link=$td.attr('link')==undefined?$td.prop('link'):$td.attr('link');
				return link;
    		},
    		clearHyperlink:function(){
    			var r = priv.selStart.row;
        		var c = priv.selStart.col;
        		var $td = $(grid.getCellAtCoords({row: r,col: c}));
        		var link=$td.attr('link')==undefined?$td.prop('link'):$td.attr('link');
        		if(link){
        			$td.attr('link',null);
        			var html = $($td.find('a')[0]).html();
        			$td.html(html);
        		}
    		},
    		setFrameid:function(str,text){
    			var r = priv.selStart.row;
        		var c = priv.selStart.col;
        		var $td = $(grid.getCellAtCoords({row: r,col: c}));
        		$td.html("");
        		$td.append("subreport:"+text);
        		$td.attr('frameid',str);
        		var XMLData = Handsontable.XML.parsingToXML();
	      		$tableModel.val(XMLData);
    		},
    		getFrameid:function(){
    			var r = priv.selStart.row;
        		var c = priv.selStart.col;
        		var $td = $(grid.getCellAtCoords({row: r,col: c}));
        		var frameid=$td.attr('frameid')==undefined?$td.prop('frameid'):$td.attr('frameid');
				return frameid;
    		},
    		clearFrameid:function(){
    			var r = priv.selStart.row;
        		var c = priv.selStart.col;
        		var $td = $(grid.getCellAtCoords({row: r,col: c}));
        		var frameid=$td.attr('frameid')==undefined?$td.prop('frameid'):$td.attr('frameid');
        		if(frameid){
        			$td.attr('frameid',null);
        			//var html = $($td.find('a')[0]).html();
        			$td.html("");
        		}
    		},
    		setFillProperties:function(str){
    			var r = priv.selStart.row;
        		var c = priv.selStart.col;
        		var $td = $(grid.getCellAtCoords({row: r,col: c}));
        		$td.attr('relatedid',str);
        		var XMLData = Handsontable.XML.parsingToXML();
		  		$tableModel.val(XMLData);
    		},
    		getFillProperties:function(){
    			var r = priv.selStart.row;
        		var c = priv.selStart.col;
        		var $td = $(grid.getCellAtCoords({row: r,col: c}));
        		var id=$td.attr('relatedid')==undefined?"":$td.attr('relatedid');
        		var rowAndCol = r+"-"+c;
				return id+","+rowAndCol;
    		},
    }
  //TODO 函数
    Handsontable.dataFormat = {
    		setDataFormat : function(nm,data,v){
    			data = eval("("+data+")");
    			var r = priv.selStart.row;
        		var c = priv.selStart.col;
        		var $td = $(grid.getCellAtCoords({row: r,col: c}));
        		if($(parent.rprop.document.getElementById("dsvt")).is(":hidden")){
        			var dataXml = document.getElementById("dataSetsModel").value;
        			var domParser = new DOMParser();
        			var xmlStrDoc = domParser.parseFromString(dataXml, "text/xml");
        			for(var i=0;i<xmlStrDoc.childNodes[0].childNodes.length;i++){
        				if(xmlStrDoc.childNodes[0].childNodes[i].getAttribute('name')==parent.rprop.$('#ds').val()){
        					var fieldsArr = xmlStrDoc.childNodes[0].childNodes[i].getElementsByTagName("fields")[0].childNodes;	
        					for(var j=0;j<fieldsArr.length;j++){
        						if(fieldsArr[j].getAttribute('Name')==parent.rprop.$('#dsvs').val()){
        							fieldsArr[j].getElementsByTagName("datafield")[0].setAttribute("formatValue",v);
        							fieldsArr[j].getElementsByTagName("datafield")[0].setAttribute("formatType",data.name);
        						}
        					}
        				}
        			}
        			$td.attr("fieldFormat",v);
        			var XMLData = Handsontable.XML.parsingToXML();
    		  		$tableModel.val(XMLData);
        			parent.rprop.document.getElementById(nm).value=v;
        			document.getElementById("dataSetsModel").value = xmlStrDoc.childNodes[0].outerHTML;
    		  		parent.closedialog();
    		  		return;
    			}
        		if(v==""){
        			var fm=$td.attr('format')==undefined?$td.prop('format'):$td.attr('format');
        			fm = eval("("+fm+")");
        			$.ajax({
            			url : "convertDataFormat.action", 
            			type:"post",
            			async: false,
            			data:{
            				name : fm.name,
            				type : fm.type,
            				newType:v,
            				value : $td.html(),
            			},
            			success : function(dat) {
            				dat = eval("("+dat+")");
            				if(dat==null) {
            					parent.closedialog();
            					return;
            				}
            				grid.populateFromArray(priv.selStart, [[dat]], null, false, 'edit');
            				$td.attr('format',"");
            				$td.attr("fieldFormat","");
            				parent.rprop.document.getElementById(nm).value=v;
            				var XMLData = Handsontable.XML.parsingToXML();
            		  		$tableModel.val(XMLData);
            		  		parent.rtree.initModelTree();
            		  		parent.closedialog();
            			}
            		});
        			return;
        		}
        		var format=$td.attr('format')==undefined?$td.prop('format'):$td.attr('format');
        		var type;
				if(format){
					type = eval("("+format+")").type;
				}else{
					type = "";
				}
        		$.ajax({
        			url : "convertDataFormat.action", 
        			type:"post",
        			data:{
        				name : data.name,
        				type : type,
        				newType:data.type,
        				value : $td.html(),
        			},
        			success : function(dat) {
        				dat = eval("("+dat+")");
        				if(dat==null) {
        					parent.closedialog();
        					return;
        				}
        				$td.attr("format",JSON.stringify(data));
        				$td.attr("fieldFormat",v);
        				grid.populateFromArray(priv.selStart, [[dat]], null, false, 'edit');
        				parent.rprop.document.getElementById(nm).value=data.type;
        				var XMLData = Handsontable.XML.parsingToXML();
        		  		$tableModel.val(XMLData);
        		  		parent.rtree.initModelTree();
            			parent.closedialog();
        			}
        		});
    		},
    		setDic:function(v){
    			var r = priv.selStart.row;
        		var c = priv.selStart.col;
        		var $td = $(grid.getCellAtCoords({row: r,col: c}));
        		$td.attr('dic',v);
        		var XMLData = Handsontable.XML.parsingToXML();
		  		$tableModel.val(XMLData);
    		},
    		initDic:function(){
    			var obj=$($dataSetsModel.val());
    			var FUNC = [];
    	        var dicQu = function (dictype,dataSetName,datasourcename,sql) {
    	            $.ajax({
    	            	url : "../../selectDic.action", 
	        			type:"post",
	        			async:true,
	        			data:{
	        				dataSourceName : datasourcename,
	        				SQLText : sql
	        			},
	        			success : function(data) {
	        				data = eval("("+data+")");
	        				data = eval("("+data+")");
	        				var dic = {};
	        		 		for(var i=0;i<data.length;i++){
	        		 			if(dic[data[i][dictype.type]]==null){
	        		 				dic[data[i][dictype.type]] = {};
	        		 				dic[data[i][dictype.type]][data[i][dictype.code]] = data[i][dictype.text];
	        		 			}else{
	        		 				dic[data[i][dictype.type]][data[i][dictype.code]] = data[i][dictype.text];
	        		 			}
	        		 		}
	        		 		dic.dicDataType = dictype.type;
	        		 		dicData[dataSetName] = dic;
    	                    $(document).dequeue('dicqueue');//触发下个队列
    	                }
    	            });
    	        }
    	        var j=0;
    	        obj.find('dataset').each(function(i){
    	        	if($(this).attr('isdic')=="true"){
    	        		var dictype = {};
    	        		var dataSetName = $(this).attr('name');
    	        		var datasourcename = $($(this).find('query>datasourcename')[0]).html();
    	        		var sql = $($(this).find('query>commandtext')[0]).html();
    	        		$(this).find('fields>field').each(function(i){
    	        			if($(this).attr("dictype")!=null)
    	        				dictype[$(this).attr("dictype")] = $(this).attr("name");
    	        		});
    	        		
    	        		var esql=asencrypt(sql);
    	        		FUNC.push((function (j) {
    	                    return function () {
    	                    	dicQu(dictype,dataSetName,datasourcename,esql);
    	                    }
    	                })(j));
    	        		$(document).queue('dicqueue', FUNC);
    	        		j++;
    	        	}
    	        });
                $(document).dequeue('dicqueue');//触发队列
    		},
    		setExpText:function(v){
    			var r = priv.selStart.row;
        		var c = priv.selStart.col;
        		var $td = $(grid.getCellAtCoords({row: r,col: c}));
        		$td.attr('expText',v);
        		var XMLData = Handsontable.XML.parsingToXML();
		  		$tableModel.val(XMLData);
    		}
    }
  };

  var settings = {
    'rows': 5,
    'cols': 5,
    'minSpareRows': 0,
    'minSpareCols': 0,
    'minHeight': 0,
    'minWidth': 0,
    'multiSelect': true,
    'fillHandle': true,
    'undo': true,
    'enterBeginsEditing': true,
    'enterMoves': {row: 1, col: 0},
    'tabMoves': {row: 0, col: 1},
    'autoWrapRow': false,
    'autoWrapCol': false
  };

  $.fn.handsontable = function (action, options) {
    var i, ilen, args, output = [];
    if (typeof action !== 'string') { //init
      options = action;
      return this.each(function () {
        var $this = $(this);
        if ($this.data("handsontable")) {
          instance = $this.data("handsontable");
          instance.updateSettings(options);
          
        }
        else {
          var currentSettings = $.extend({}, settings), instance;
          if (options) {
            $.extend(currentSettings, options);
          }
          instance = new Handsontable.Core($this, currentSettings);
          $this.data("handsontable", instance);
          instance.init();
        }
      });
    }
    else {
      args = [];
      if (arguments.length > 1) {
        for (i = 1, ilen = arguments.length; i < ilen; i++) {
          args.push(arguments[i]);
        }
      }
      this.each(function () {
        output = $(this).data("handsontable")[action].apply(this, args);
      });
      return output;
    }
  };
})(jQuery, window, Handsontable);

/**
 * Returns true if keyCode represents a printable character
 * @param {Number} keyCode
 * @return {Boolean}
 */
Handsontable.helper.isPrintableChar = function (keyCode) {
  return ((keyCode == 32) || //space
      (keyCode >= 48 && keyCode <= 57) || //0-9
      (keyCode >= 96 && keyCode <= 111) || //numpad
      (keyCode >= 186 && keyCode <= 192) || //;=,-./`
      (keyCode >= 219 && keyCode <= 222) || //[]{}\|"'
      keyCode >= 226 || //special chars (229 for Asian chars)
      (keyCode >= 65 && keyCode <= 90)); //a-z
};


(function ($) {
  "use strict";
  /**
   * Handsontable UndoRedo class
   */
  Handsontable.UndoRedo = function (instance) {
    var that = this;
    this.instance = instance;
    this.clear();
    instance.container.on("datachange.handsontable", function (event, changes, origin) {
      if (origin !== 'undo' && origin !== 'redo') {
        that.add(changes);
      }
    });
  };

  /**
   * Undo operation from current revision
   */
  Handsontable.UndoRedo.prototype.undo = function () {
    var i, ilen;
    if (this.isUndoAvailable()) {
      var setData = $.extend(true, [], this.data[this.rev]);
      for (i = 0, ilen = setData.length; i < ilen; i++) {
        setData[i].splice(3, 1);
      }
      this.instance.setDataAtCell(setData, null, null, true, 'undo');
      this.rev--;
    }
  };

  /**
   * Redo operation from current revision
   */
  Handsontable.UndoRedo.prototype.redo = function () {
    var i, ilen;
    if (this.isRedoAvailable()) {
      this.rev++;
      var setData = $.extend(true, [], this.data[this.rev]);
      for (i = 0, ilen = setData.length; i < ilen; i++) {
        setData[i].splice(2, 1);
      }
      this.instance.setDataAtCell(setData, null, null, true, 'redo');
    }
  };

  /**
   * Returns true if undo point is available
   * @return {Boolean}
   */
  Handsontable.UndoRedo.prototype.isUndoAvailable = function () {
    return (this.rev >= 0);
  };

  /**
   * Returns true if redo point is available
   * @return {Boolean}
   */
  Handsontable.UndoRedo.prototype.isRedoAvailable = function () {
    return (this.rev < this.data.length - 1);
  };

  /**
   * Add new history poins
   * @param changes
   */
  Handsontable.UndoRedo.prototype.add = function (changes) {
    this.rev++;
    this.data.splice(this.rev); //if we are in point abcdef(g)hijk in history, remove everything after (g)
    this.data.push(changes);
  };

  /**
   * Clears undo history
   */
  Handsontable.UndoRedo.prototype.clear = function () {
    this.data = [];
    this.rev = -1;
  };
})(jQuery);
(function ($) {
  "use strict";
  /**
   * Handsontable BlockedRows class
   * @param {Object} instance
   */
  Handsontable.BlockedRows = function (instance) {
    this.instance = instance;
    this.headers = [];
    var position = instance.table.position();
    instance.positionFix(position);
    this.main = $('<div id ="colHeaderDiv" style="position: absolute; top: ' + position.top + 'px; left: ' + position.left + 'px"><table cellspacing="0" cellpadding="0"><thead></thead></table></div>');
    this.instance.container.append(this.main);
    this.hasCSS3 = !($.browser.msie && (parseInt($.browser.version, 10) <= 8)); //Used to get over IE8- not having :last-child selector
    this.update();
  };

  /**
   * Returns number of blocked cols
   */
  Handsontable.BlockedRows.prototype.count = function () {
    return this.headers.length;
  };

  /**
   * Create column header in the grid table
   */
  Handsontable.BlockedRows.prototype.createCol = function (className) {
    var $tr, th, h, hlen = this.count();
    for (h = 0; h < hlen; h++) {
      $tr = this.main.find('thead tr.' + this.headers[h].className);
      if (!$tr.length) {
        $tr = $('<tr class="' + this.headers[h].className + '"></tr>');
        this.main.find('thead').append($tr);
      }
      $tr = this.instance.table.find('thead tr.' + this.headers[h].className);
      if (!$tr.length) {
        $tr = $('<tr class="' + this.headers[h].className + '"></tr>');
        this.instance.table.find('thead').append($tr);
      }

      th = document.createElement('th');
      th.className = this.headers[h].className;
      if (className) {
        th.className += ' ' + className;
      }
      th.innerHTML = this.headerText('&nbsp;');
      this.instance.minWidthFix(th);
      this.instance.table.find('thead tr.' + this.headers[h].className)[0].appendChild(th);

      th = document.createElement('th');
      th.className = this.headers[h].className;
      if (className) {
        th.className += ' ' + className;
      }
      this.instance.minWidthFix(th);
      this.main.find('thead tr.' + this.headers[h].className)[0].appendChild(th);
    }
  };

  /**
   * Create column header in the grid table
   */
  Handsontable.BlockedRows.prototype.create = function () {
    var c;
    if (this.count() > 0) {
      this.instance.table.find('thead').empty();
      this.main.find('thead').empty();
      var offset = this.instance.blockedCols.count();
      for (c = offset - 1; c >= 0; c--) {
        this.createCol(this.instance.blockedCols.headers[c].className);
      }
      for (c = 0; c < this.instance.colCount; c++) {
        this.createCol();
      }
    }
    if (!this.hasCSS3) {
      this.instance.container.find('thead tr.lastChild').not(':last-child').removeClass('lastChild');
      this.instance.container.find('thead tr:last-child').not('.lastChild').addClass('lastChild');
    }
  };

  /**
   * Copy table column header onto the floating layer above the grid
   */
  Handsontable.BlockedRows.prototype.refresh = function () {
    var label;
    if (this.count() > 0) {
      var that = this;
      var hlen = this.count(), h;
      for (h = 0; h < hlen; h++) {
        var $tr = this.main.find('thead tr.' + this.headers[h].className);
        var tr = $tr[0];
        var ths = tr.childNodes;
        var thsLen = ths.length;
        var offset = this.instance.blockedCols.count();

        while (thsLen > this.instance.colCount + offset) {
          //remove excessive cols
          thsLen--;
          $(tr.childNodes[thsLen]).remove();
        }

        for (h = 0; h < hlen; h++) {
          var realThs = this.instance.table.find('thead th.' + this.headers[h].className);
          for (var i = 0; i < thsLen; i++) {
            label = that.headers[h].columnLabel(i - offset);
            if (this.headers[h].format && this.headers[h].format === 'small') {
              realThs[i].innerHTML = this.headerText(label);
              ths[i].innerHTML = this.headerText(label);
            }
            else {
              realThs[i].innerHTML = label;
              ths[i].innerHTML = label;
            }
            this.instance.minWidthFix(realThs[i]);
            this.instance.minWidthFix(ths[i]);
            ths[i].style.minWidth = realThs.eq(i).width() + 'px';
          }
        }
      }

      this.ths = this.main.find('tr:last-child th');
      this.refreshBorders();
    }
  };

  /**
   * Refresh border width
   */
  Handsontable.BlockedRows.prototype.refreshBorders = function () {
    if (this.count() > 0) {
      if (this.instance.curScrollTop === 0) {
        this.ths.css('borderBottomWidth', 0);
      }
      else if (this.instance.lastScrollTop === 0) {
        this.ths.css('borderBottomWidth', '1px');
      }
    }
  };

  /**
   * Recalculate column widths on the floating layer above the grid
   * @param {Object} changes
   */
  Handsontable.BlockedRows.prototype.dimensions = function (changes) {
    if (this.count() > 0) {
      var offset = this.instance.blockedCols.count();
      for (var i = 0, ilen = changes.length; i < ilen; i++) {
        this.ths[changes[i][1] + offset].style.minWidth = $(this.instance.getCell(changes[i][0], changes[i][1])).width() + 'px';
      }
    }
  };


  /**
   * Update settings of the column header
   */
  Handsontable.BlockedRows.prototype.update = function () {
    this.create();
    this.refresh();
  };

  /**
   * Add column header to DOM
   */
  Handsontable.BlockedRows.prototype.addHeader = function (header) {
    for (var h = this.count() - 1; h >= 0; h--) {
      if (this.headers[h].className === header.className) {
        this.headers.splice(h, 1); //if exists, remove then add to recreate
      }
    }
    this.headers.push(header);
    this.headers.sort(function (a, b) {
      return a.priority || 0 - b.priority || 0
    });
    this.update();
  };

  /**
   * Remove column header from DOM
   */
  Handsontable.BlockedRows.prototype.destroyHeader = function (className) {
    for (var h = this.count() - 1; h >= 0; h--) {
      if (this.headers[h].className === className) {
        this.main.find('thead tr.' + this.headers[h].className).remove();
        this.instance.table.find('thead tr.' + this.headers[h].className).remove();
        this.headers.splice(h, 1);
      }
    }
  };

  /**
   * Puts string to small text template
   */
  Handsontable.BlockedRows.prototype.headerText = function (str) {
    return '<span class="small">' + str + '</span>';
  };
})(jQuery);
(function ($) {
  "use strict";
  /**
   * Handsontable BlockedCols class
   * @param {Object} instance
   */
  Handsontable.BlockedCols = function (instance) {
    this.heightMethod = ($.browser.mozilla || $.browser.opera) ? "outerHeight" : "height";
    this.instance = instance;
    this.headers = [];
    var position = instance.table.position();
    instance.positionFix(position);
    this.main = $('<div id="rowHeaderDiv" style="position: absolute; top: ' + position.top + 'px; left: ' + position.left + 'px"><table cellspacing="0" cellpadding="0"><thead><tr></tr></thead><tbody></tbody></table></div>');
    this.instance.container.append(this.main);
  };

  /**
   * Returns number of blocked cols
   */
  Handsontable.BlockedCols.prototype.count = function () {
    return this.headers.length;
  };

  /**
   * Create row header in the grid table
   */
  Handsontable.BlockedCols.prototype.createRow = function (tr) {
    var th;
    var mainTr = document.createElement('tr');

    for (var h = 0, hlen = this.count(); h < hlen; h++) {
      th = document.createElement('th');
      th.className = this.headers[h].className;
      this.instance.minWidthFix(th);
      tr.insertBefore(th, tr.firstChild);

      th = document.createElement('th');
      th.className = this.headers[h].className;
      mainTr.insertBefore(th, mainTr.firstChild);
    }

    this.main.find('tbody')[0].appendChild(mainTr);
  };

  /**
   * Create row header in the grid table
   */
  Handsontable.BlockedCols.prototype.create = function () {
    var hlen = this.count(), h, th;
    this.main.find('tbody').empty();
    this.instance.table.find('tbody th').remove();
    var $theadTr = this.main.find('thead tr');
    $theadTr.empty();

    if (hlen > 0) {
      var offset = this.instance.blockedRows.count();
      if (offset) {
        for (h = 0; h < hlen; h++) {
          th = $theadTr[0].getElementsByClassName ? $theadTr[0].getElementsByClassName(this.headers[h].className)[0] : $theadTr.find('.' + this.headers[h].className.replace(/\s/i, '.'))[0];
          if (!th) {
            th = document.createElement('th');
            th.className = this.headers[h].className;
            th.innerHTML = this.headerText('&nbsp;');
            this.instance.minWidthFix(th);
            $theadTr[0].insertBefore(th, $theadTr[0].firstChild);
          }
        }
      }

      var trs = this.instance.table.find('tbody')[0].childNodes;
      for (var r = 0; r < this.instance.rowCount; r++) {
        this.createRow(trs[r]);
      }
    }
  };

  /**
   * Copy table row header onto the floating layer above the grid
   */
  Handsontable.BlockedCols.prototype.refresh = function () {
    var hlen = this.count(), h, th, realTh, i, label;
    if (hlen > 0) {
      var $tbody = this.main.find('tbody');
      var tbody = $tbody[0];
      var trs = tbody.childNodes;
      var trsLen = trs.length;
      while (trsLen > this.instance.rowCount) {
        //remove excessive rows
        trsLen--;
        $(tbody.childNodes[trsLen]).remove();
      }

      var realTrs = this.instance.table.find('tbody tr');
      for (i = 0; i < trsLen; i++) {
        for (h = 0; h < hlen; h++) {
          label = this.headers[h].columnLabel(i);
          realTh = realTrs[i].getElementsByClassName ? realTrs[i].getElementsByClassName(this.headers[h].className)[0] : $(realTrs[i]).find('.' + this.headers[h].className.replace(/\s/i, '.'))[0];
          th = trs[i].getElementsByClassName ? trs[i].getElementsByClassName(this.headers[h].className)[0] : $(trs[i]).find('.' + this.headers[h].className.replace(/\s/i, '.'))[0];
          if (this.headers[h].format && this.headers[h].format === 'small') {
            realTh.innerHTML = this.headerText(label);
            th.innerHTML = this.headerText(label);
          }
          else {
            realTh.innerHTML = label;
            th.innerHTML = label;
          }
          this.instance.minWidthFix(th);
          th.style.height = $(realTh)[this.heightMethod]() + 'px';
          $(th).parent().css('height',$(realTh)[this.heightMethod]() + 'px');
        }
      }

      this.ths = this.main.find('th:last-child');
      this.refreshBorders();
    }
  };

  /**
   * Refresh border width
   */
  Handsontable.BlockedCols.prototype.refreshBorders = function () {
    if (this.count() > 0) {
      if (this.instance.curScrollLeft === 0) {
        this.ths.css('borderRightWidth', 0);
      }
      else if (this.instance.lastScrollLeft === 0) {
        this.ths.css('borderRightWidth', '1px');
      }
    }
  };

  /**
   * Recalculate row heights on the floating layer above the grid
   * @param {Object} changes
   */
  Handsontable.BlockedCols.prototype.dimensions = function (changes) {
    if (this.count() > 0) {
      var trs = this.main[0].firstChild.getElementsByTagName('tbody')[0].childNodes;
      for (var i = 0, ilen = changes.length; i < ilen; i++) {
        var $th = $(this.instance.getCell(changes[i][0], changes[i][1]));
        if ($th.length) {
          trs[changes[i][0]].firstChild.style.height = $th[this.heightMethod]() + 'px';
        }
      }
    }
  };

  /**
   * Update settings of the row header
   */
  Handsontable.BlockedCols.prototype.update = Handsontable.BlockedRows.prototype.update;

  /**
   * Add row header to DOM
   */
  Handsontable.BlockedCols.prototype.addHeader = function (header) {
    for (var h = this.count() - 1; h >= 0; h--) {
      if (this.headers[h].className === header.className) {
        this.headers.splice(h, 1); //if exists, remove then add to recreate
      }
    }
    this.headers.push(header);
    this.headers.sort(function (a, b) {
      return a.priority || 0 - b.priority || 0
    });
  };

  /**
   * Remove row header from DOM
   */
  Handsontable.BlockedCols.prototype.destroyHeader = function (className) {
    for (var h = this.count() - 1; h >= 0; h--) {
      if (this.headers[h].className === className) {
        this.headers.splice(h, 1);
      }
    }
  };

  /**
   * Puts string to small text template
   */
  Handsontable.BlockedCols.prototype.headerText = Handsontable.BlockedRows.prototype.headerText;
})(jQuery);
(function ($) {
  "use strict";
  /**
   * Handsontable RowHeader extension
   * @param {Object} instance
   * @param {Array|Boolean} [labels]
   */
  Handsontable.RowHeader = function (instance, labels) {
    var that = this;
    var start,end;
    this.className = 'htRowHeader';
    instance.blockedCols.main.on('mousedown', 'th.htRowHeader', function (event) {
    	if(isMoveDown) return;
    	if(isChangeWidth||isChangeHeight) return;
    	isThHander = true;
      if (!$(event.target).hasClass('btn') && !$(event.target).hasClass('btnContainer')) {
        instance.deselectCell();
        instance.removeSelectStyle();
        var $t = $('#colHeaderDiv').find('th');
		$t.removeClass('c-active');
		$t.css("border-right-color","#CCC");
		var $ts = $('#rowHeaderDiv').find('th');
	    $ts.removeClass('r-active');
	   $ts.css("border-bottom-color","#CCC");
        $(this).addClass('r-active');
        that.lastActive = this;
        var offset = instance.blockedRows.count();
        start=this.parentNode.rowIndex - offset;
        instance.selectCell(this.parentNode.rowIndex - offset, 0, this.parentNode.rowIndex - offset, instance.colCount - 1, false);
      }
      parent.rprop.SynProperties('row',{start:{row:this.parentNode.rowIndex - offset,col:0},end:{row:this.parentNode.rowIndex - offset,col:instance.colCount - 1}});
    });
    instance.blockedCols.main.on('mouseover', 'th.htRowHeader', function (){
		if(isThHander){
			var $th = $(this);
//		    $th.addClass('active');
		    that.lastActive = this;
		    var index = $th.index();
		    var offset = instance.blockedRows.count();
		    end =this.parentNode.rowIndex - offset;
		    var $t = $('#rowHeaderDiv').find('th');
       		$t.removeClass('r-active');
       		$t.css("border-bottom-color","#CCC");
       		if(start>end){
       			for(var i=end;i<=start;i++){
    		    	var th = $($(this).parent().parent()[0].children[i].children[0]);
    		    	 th.addClass('r-active');
    		    	 if(i<start)
    		    	 th.css("border-bottom-color","#FFF");
    		    }
       			instance.selectCell(start, 0, end, instance.colCount - 1, false);
       			parent.rprop.SynProperties('row',{start:{row:start,col:0},end:{row:end,col:instance.colCount - 1}});
       		 }else{
       			for(var i=start;i<=end;i++){
    		    	var th = $($(this).parent().parent()[0].children[i].children[0]);
    		    	 th.addClass('r-active');
    		    	 if(i<end)
        		    	 th.css("border-bottom-color","#FFF");
    		    }
       			instance.selectCell(start, 0, end, instance.colCount - 1, false);
       			parent.rprop.SynProperties('row',{start:{row:start,col:0},end:{row:end,col:instance.colCount - 1}});
       		 }
		 }
	});
    instance.container.on('deselect.handsontable', function () {
      that.deselect();
    });
    this.labels = labels;
    this.instance = instance;
    this.instance.rowHeader = this;
    this.format = 'small';
    instance.blockedCols.addHeader(this);
  };

  /**
   * Return custom row label or automatically generate one
   * @param {Number} index Row index
   * @return {String}
   */
  Handsontable.RowHeader.prototype.columnLabel = function (index) {
    if (typeof this.labels[index] !== 'undefined') {
      return this.labels[index];
    }
    return index + 1;
  };

  /**
   * Remove current highlight of a currently selected row header
   */
  Handsontable.RowHeader.prototype.deselect = function () {
    if (this.lastActive) {
      $(this.lastActive).removeClass('r-active')
      $(this.lastActive).removeClass('c-active');
      var $t = $('#colHeaderDiv').find('th');
 		$t.removeClass('c-active');
 		$t.css("border-right-color","#CCC");
     var $ts = $('#rowHeaderDiv').find('th');
 	    $ts.removeClass('r-active');
 	   $ts.css("border-bottom-color","#CCC");
      this.lastActive = null;
    }
  };

  /**
   *
   */
  Handsontable.RowHeader.prototype.destroy = function () {
    this.instance.blockedCols.destroyHeader(this.className);
  };
})(jQuery);
(function ($) {
  "use strict";
  /**
   * Handsontable ColHeader extension
   * @param {Object} instance
   * @param {Array|Boolean} [labels]
   */
  Handsontable.ColHeader = function (instance, labels) {
    var that = this;
    var start,end;
    this.className = 'htColHeader';
    instance.blockedRows.main.on('mousedown', 'th.htColHeader', function () {
    	if(isMoveDown) return;
    	if($(this).attr('class')=="htColHeader htRowHeader"){
    		instance.selectAllCell();
    		return;
    	}
    	if(isChangeWidth||isChangeHeight) return;
    	isThHander = true;
      instance.deselectCell();
      instance.removeSelectStyle();
      var $t = $('#colHeaderDiv').find('th');
		$t.removeClass('c-active');
		$t.css("border-right-color","#CCC");
   var $ts = $('#rowHeaderDiv').find('th');
	    $ts.removeClass('r-active');
	   $ts.css("border-bottom-color","#CCC");
      var $th = $(this);
      $th.addClass('c-active');
      that.lastActive = this;
      var index = $th.index();
      var offset = instance.blockedCols ? instance.blockedCols.count() : 0;
      start = index - offset;
      instance.selectCell(0, index - offset, instance.rowCount - 1, index - offset, false);
      parent.rprop.SynProperties('col',{start:{row:0,col:index - offset},end:{row:instance.rowCount - 1,col:index - offset}});
    });
    instance.blockedRows.main.on('mouseover', 'th.htColHeader', function (){
		if(isThHander){
			var $th = $(this);
//		    $th.addClass('active');
		    that.lastActive = this;
		    var index = $th.index();
		    var offset = instance.blockedCols ? instance.blockedCols.count() : 0;
		    end = index - offset;
		    var $t = $('#colHeaderDiv').find('th');
       		$t.removeClass('c-active');
       		$t.css("border-right-color","#CCC");
       		if(start>end){
       			for(var i=end;i<=start;i++){
    		    	var th = $($(this).parent()[0].children[i+1]);
    		    	 th.addClass('c-active');
    		    	 if(i<start)
    		    	 th.css("border-right-color","#FFF");
    		    }
       			instance.selectCell(0, start, instance.rowCount - 1, end, false);
    		    parent.rprop.SynProperties('col',{start:{row:0,col:start},end:{row:instance.rowCount - 1,col:end}});
       		}else{
       			for(var i=start;i<=end;i++){
    		    	var th = $($(this).parent()[0].children[i+1]);
    		    	 th.addClass('c-active');
    		    	 if(i<end)
    		    	 th.css("border-right-color","#FFF");
    		    }
       			instance.selectCell(0, start, instance.rowCount - 1, end, false);
    		    parent.rprop.SynProperties('col',{start:{row:0,col:start},end:{row:instance.rowCount - 1,col:end}});
       		}
		 }
	});
    instance.container.on('deselect.handsontable', function () {
      that.deselect();
    });
    this.instance = instance;
    this.labels = labels;
    this.instance.colHeader = this;
    this.format = 'small';
    instance.blockedRows.addHeader(this);
  };

  /**
   * Return custom column label or automatically generate one
   * @param {Number} index Row index
   * @return {String}
   */
  Handsontable.ColHeader.prototype.columnLabel = function (index) {
    if (typeof this.labels[index] !== 'undefined') {
      return this.labels[index];
    }
    var dividend = index + 1;
    var columnLabel = '';
    var modulo;
    while (dividend > 0) {
      modulo = (dividend - 1) % 26;
      columnLabel = String.fromCharCode(65 + modulo) + columnLabel;
      dividend = parseInt((dividend - modulo) / 26);
    }
    return columnLabel;
  };

  /**
   * Remove current highlight of a currently selected column header
   */
  Handsontable.ColHeader.prototype.deselect = Handsontable.RowHeader.prototype.deselect;

  /**
   *
   */
  Handsontable.ColHeader.prototype.destroy = function () {
    this.instance.blockedRows.destroyHeader(this.className);
  };
})(jQuery);


