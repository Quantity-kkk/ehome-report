var xmlStrDoc = null;
var treeObj;
$(document).ready(function(){
	 var $tableModel = $('#tableModel');
});
function initModelTree(){
	var tableData = parent.rdes.Handsontable.XML.getTableXML();
	treeObj = $.fn.zTree.getZTreeObj("treed");
	//更新
	var nodes = treeObj.getNodes();
	for(var i=0;i<nodes[0].children.length;i++){
		treeObj.removeChildNodes(nodes[0].children[i]);
	}
	var jsonp = null;
	var maxCol=-1;
	var rowCount=0;
	var count=1;
	if (window.DOMParser) {
		var domParser = new DOMParser();
		xmlStrDoc = domParser.parseFromString(tableData, "text/xml");
		for(var r=0;r<xmlStrDoc.childNodes[0].childNodes.length;r++){
			var nodeRow = xmlStrDoc.childNodes[0].childNodes[r];
			var category = nodeRow.getAttribute("category");
			for(var c=0;c<nodeRow.childNodes.length;c++){
				var xNode,tnode,newNode,id;
				var nodeCol = getElement(r, c);
				if(category=='headtitle'){//头标题
					if(nodeCol.textContent=="") continue;
					tnode = treeObj.getNodesByParam("id", "10", null);
					id = "10";
					if(!tnode[0].isParent){
						id += 1;
					}else{
						id += tnode[0].children.length+1;
					}
					xNode ={ id:id, name:nodeCol.textContent,coords: r+","+c};
					treeObj.addNodes(tnode[0],xNode,true);
				}else if(category=='reporthead'){//报表头
					if(nodeCol.textContent=="") continue;
					if(maxCol<c) maxCol=c;
					tnode = treeObj.getNodesByParam("id", "20", null);
					id = "20";
					if(!tnode[0].isParent){
						id += 1;
					}else{
						id += tnode[0].children.length+1;
					}
					xNode ={ id:id, name:nodeCol.textContent,coords: r+","+c};
					var pNode = getMultiHeaders(r, c,category);
					if(pNode!=false&&pNode!=true){
						//TODO 插入树节点
						pNode = treeObj.getNodesByParam("name", pNode.textContent, null);
						treeObj.addNodes(pNode[0],xNode,true);
					}else if(pNode ==true){
						treeObj.addNodes(tnode[0],xNode,true);
					}
				}else if(category=='dataarea'){//数据区
					if(maxCol<c) continue;
					tnode = treeObj.getNodesByParam("id", "30", null);
					id = "30";
					if(!tnode[0].isParent){
						id += 1;
					}else{
						id += tnode[0].children.length+1;
					}
					if(rowCount<r){
						xNode ={ id:"r"+id, name:"第"+count+"行",coords: r+","+c};
						newNode = treeObj.addNodes(tnode[0],xNode,true);
						xNode ={ id:id, name:nodeCol.textContent,coords: r+","+c};
						treeObj.addNodes(newNode[0],xNode,true);
						rowCount = r;
						count++;
					}else{
						xNode ={ id:id, name:nodeCol.textContent,coords: r+","+c};
						treeObj.addNodes(newNode[0],xNode,true);
					}
				}else if(category=='reportfoot'){//报表尾
					tnode = treeObj.getNodesByParam("id", "40", null);
					id = "40";
					if(!tnode[0].isParent){
						id += 1;
					}else{
						id += tnode[0].children.length+1;
					}
					xNode ={ id:id, name:nodeCol.textContent,coords: r+","+c};
					treeObj.addNodes(tnode[0],xNode,true);
					
				}else if(category=='foottitle'){//尾标题
					
				}
				
			}
		}
	}else {
		alert("您的浏览器找不到所需的XML解析器。");
	}
}
function getElement(row,col){
	return xmlStrDoc.childNodes[0].childNodes[row].childNodes[col];
}
function getMultiHeaders(r,c,category){
	for(var i=r-1;;i--){
		if(getElement(r, c).getAttribute("ishidden")) return false;
		if(i<0){
			return true;
		}
		var node = getElement(i, c);
		if(xmlStrDoc.childNodes[0].childNodes[i].getAttribute("category")!=category) return true;
		if(node.getAttribute("colspan")>1){
//			var endCol = c +parseInt(node.getAttribute("colspan"))-1;
			return node;
		}else if(node.getAttribute("ishidden")){
			var coords = node.getAttribute("coords");
			coords = coords.split(',')[2].split('#');
			return getElement(coords[0], coords[1]);
		}
//		else{
//			return true;
//		}	
	}
}

//索引选中
function selectByParam(row,col){
	treeObj = $.fn.zTree.getZTreeObj("treed");
     var nodes = treeObj.getNodesByParam("coords", row+","+col, null);
     if(nodes.length>0){
         for(var i=0;i<nodes.length;i++){
             treeObj.selectNode(nodes[i]);
             treeObj.expandNode(nodes[i], false, true, true);
         }
     }
}
function updateNode(row,col,value){
	 var nodes = treeObj.getNodesByParam("coords", row+","+col, null);
	 if(nodes.length>0){
         for(var i=0;i<nodes.length;i++){
        	nodes[i].name = value;
        	treeObj.updateNode(nodes[i]);
         }
     }
}
