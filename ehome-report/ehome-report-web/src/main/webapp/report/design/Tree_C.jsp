<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <title></title>
    <link rel="stylesheet" href="../lib/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <style type="text/css">
        body {
            margin: 0;
            padding: 0;
        }

        .ztree li span.button.home_ico_open, .ztree li span.button.home_ico_close {
            margin-right: 2px;
            background: url(../lib/ztree/css/zTreeStyle/img/diy/1_open.png) no-repeat scroll 0 0 transparent;
            vertical-align: top;
            *vertical-align: middle
        }

        .ztree li span.button.pIcon01_ico_close {
            margin-right: 2px;
            background: url(../lib/ztree/css/zTreeStyle/img/diy/1_close.png) no-repeat scroll 0 0 transparent;
            vertical-align: top;
            *vertical-align: middle
        }
        .ztree li span.button.add {margin-left:2px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
    </style>
    <script src="../lib/jquery.min.js"></script>
    <script type="text/javascript" src="../lib/ztree/jquery.ztree.min.js"></script>
    <script src="../js/modelTreeC.js"></script>
    <script type="text/javascript">
        var setting = {
            data: {
                simpleData: {
                    enable: true
                }
            },
            view: {
                addHoverDom: addHoverDom,
                removeHoverDom: removeHoverDom
            },
                edit: {
                enable: true,
                editNameSelectAll: false,
                showRemoveBtn: showRemoveBtn,
                showRenameBtn: showRenameBtn
            },
            callback: {
                beforeRemove: beforeRemove,
                onClick: onClick,
                onRemove: onRemove
            }
        };

        var zNodes =[
            { id:"1", name:"图表集",isParent:true, open:true, iconSkin:"home"}
        ];
        $(document).ready(function(){
            var tree = $.fn.zTree.init($("#treed"), setting, zNodes);
        });
        function onClick(event, treeId, treeNode, clickFlag){
            if(treeNode.pId=="1"){
                parent.rdes.selectChart(treeNode.id);
                parent.rprop.showser(false,treeNode.id,'');
            }
            else if(treeNode.pId!=null){
                parent.rdes.selectChart(treeNode.pId);
                parent.rprop.showser(true,treeNode.pId,treeNode.id);
            }else{
                parent.rprop.showser(true,'','');
            }
        }
        var newCount = 1;
        function addHoverDom(treeId, treeNode) {
            if(treeNode.pId==1) {
                var sObj = $("#" + treeNode.tId + "_span");
                if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
                var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                        + "' title='添加数据序列' onfocus='this.blur();'></span>";
                sObj.after(addStr);
                var btn = $("#addBtn_" + treeNode.tId);
                if (btn) btn.bind("click", function () {
                    var zTree = $.fn.zTree.getZTreeObj("treed");
                    var nn="数据序列" + (newCount++);
                    var guid=parent.guid();
                    zTree.addNodes(treeNode, {id: guid, pId: treeNode.id, name: nn});

                    var obj=$(parent.rdes.getTM());
                    obj.find('chart[uuid="'+treeNode.id+'"]').each(function() {
                        var category = $(this).attr("category");
                        var str = '<serie id="'+guid+'"><name>' + nn + '</name><type>' + category + '</type><data>[]</data><markLine><type>average</type><name>自定义名字</name></markLine></serie>';
                        $(this).find('series').append(str);
                    });
                    parent.rdes.updateChart(obj.html());

                    return false;
                });
            }
        }
        function removeHoverDom(treeId, treeNode) {
            $("#addBtn_"+treeNode.tId).unbind().remove();
        }
        function showRenameBtn(treeId, treeNode) {
            return false;
        }
        function showRemoveBtn(treeId, treeNode) {
            if(treeNode.pId!=null) {
                return true;
            }else{
                return false;
            }
        }
        function beforeRemove(treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("treed");
            zTree.selectNode(treeNode);
            return confirm("确认删除 节点 -- " + treeNode.name + " 吗？\n删除将不可恢复！");
        }
        function onRemove(e, treeId, treeNode) {
            alert(getTime()+" 删除节点： " + treeNode.name);
        }
    </script>
</head>
<body>
<ul id="treed" class="ztree"></ul>
</body>
</html>