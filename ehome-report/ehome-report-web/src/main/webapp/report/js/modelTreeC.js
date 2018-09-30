var xmlStrDoc = null;
function initModelTree(){
    var treeObj = $.fn.zTree.getZTreeObj("treed");
    var nodes = treeObj.getNodesByParam("pId", "1", null);
    for (var i = 0, l = nodes.length; i < l; i++) {
        treeObj.removeNode(nodes[i]);
    }
    var obj=$(parent.rdes.getTM());
    obj.find('chart').each(function() {
        var category = $(this).attr("category");//line|bar|k|pie|radar|chord|gauge
        var uuid=$(this).attr("uuid");
        var pnode = treeObj.getNodesByParam("id", "1", null);
        switch (category) {
            case "line":
                treeObj.addNodes(pnode[0], {id: uuid,pId:pnode[0].id, name: "折线报表"}, true);
                break;
            case "bar":
                treeObj.addNodes(pnode[0], {id: uuid,pId:pnode[0].id, name: "柱状报表"}, true);
                break;
            case "k":
                treeObj.addNodes(pnode[0], {id: uuid,pId:pnode[0].id,name: "K线报表"}, true);
                break;
            case "pie":
                treeObj.addNodes(pnode[0], {id: uuid,pId:pnode[0].id, name: "饼图报表"}, true);
                break;
            case "radar":
                treeObj.addNodes(pnode[0], {id: uuid,pId:pnode[0].id,name: "雷达报表"}, true);
                break;
            case "chord":
                treeObj.addNodes(pnode[0], {id: uuid,pId:pnode[0].id, name: "和弦报表"}, true);
                break;
            case "gauge":
                treeObj.addNodes(pnode[0], {id: uuid,pId:pnode[0].id, name: "仪表盘报表"}, true);
                break;
        }
        $(this).find('serie').each(function(){
            var fnode = treeObj.getNodesByParam("id", uuid, null);
            treeObj.addNodes(fnode[0], {
                id: $(this).attr('id'),
                pId:fnode[0].id,
                name: $(this).children('name').text()
            }, true);
        });
    });

}

//索引选中
function selectByParam(nid){
    var treeObj = $.fn.zTree.getZTreeObj("treed");
    var nodes = treeObj.getNodesByParam("id", nid, null);
    if(nodes.length>0){
        treeObj.selectNode(nodes[0]);
        treeObj.expandNode(nodes[0],true,true,false,null);
        if(nodes[0].pId!=null){
            parent.rprop.showser(false,nodes[0].id,'');
        }else{
            parent.rprop.showser(true,'','');
        }
    }
}
