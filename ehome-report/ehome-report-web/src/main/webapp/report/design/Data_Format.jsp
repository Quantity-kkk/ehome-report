<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <script type="text/javascript" src="../lib/jquery.min.js"></script>
    <script type="text/javascript">
        var nm="format";
        var data;
        $(function(){
            sl('0');
        	var val = parent.rprop.document.getElementById(nm).value;
            $('#formv').val(val);
            data='{"name":"0","type":"'+val+'"}';
        });
        function sl(v){
            $('#b').empty();
            var arrv=['#0.00','#.00','#.#','#0.000','#.000','#,##0.00','#,###.00','#,###.#'];
            var arrc=['¥#0.00','¥#.00','¥#.#','¥#0.000','¥#.000','¥#,##0.00','¥#,###.00','¥#,###.#','$#0.00','#.00','$#.#','$#0.000','$#.000','$#,##0.00','$#,###.00','$#,###.#'];
            var arrd=['yyyy-MM-dd','yy-MM-dd','yyyy/MM/dd','yy/MM/dd','MMM dd,yyyy','dd,MMM,yyyy','yyyy年MM月dd日','yy年MM月dd日'];
            var arrt=['HH:mm:ss','HH:mm:ssS','kk:mm:ss','kk:mm:ssS','hh:mm:ss','hh:mm:ssS','KK:mm:ss','KK:mm:ssS'];
            var arrdt=['yyyy-MM-dd HH:mm:ss','yy-MM-dd HH:mm:ss','yyyy/MM/dd HH:mm:ss','yy/MM/dd HH:mm:ss','yyyy年MM月dd日 HH:mm:ss','yy年MM月dd日 HH:mm:ss'];
            var arrp=['#0.00%','#.00%','#.#%','#0.000%','#.000%','#,##0.00%','#,###.00%','#,###.#%'];
            var arre=['0.#E0','0.##E0','0.###E0','00.#E0','00.##E0','00.###E0','##0.#E0','##0.##E0','##0.###E0'];
            var arr;
            switch(v){
                case '0':arr=arrv;break;
                case '1':arr=arrc;break;
                case '2':arr=arrd;break;
                case '3':arr=arrt;break;
                case '4':arr=arrdt;break;
                case '5':arr=arrp;break;
                case '6':arr=arre;break;
            }
            for(i=0;i<arr.length;i++){
                $('#b').append('<option value="'+arr[i]+'">'+arr[i]+'</option>');
            }
        }
        function ck(v){
            //$('#formv').val('{"name":"'+$('#a').val()+'","type":"'+v+'"}');
            data='{"name":"'+$('#a').val()+'","type":"'+v+'"}';
            $('#formv').val(v);
        }
    </script>
    <style type="text/css">
        body{font-size:12px;marign:0;}
        input {border: 1px solid #ccc;height: 26px; margin-right: 10px;width:453px;}
        select{height: 31px;border: 1px solid #ccc;width:200px;}
        .submit{width:auto;padding:5px 15px;background: none repeat scroll 0 0 #277b9d;border: 1px solid #ccc;color:#fff;height: 30px;}
        .reset{width:auto;padding:5px 15px;background: none repeat scroll 0 0 gray;border: 1px solid #ccc;color:#fff;height: 30px;}
    </style>
</head>

<body>
<table width="100%" cellspacing="5">
    <tr>
        <td width="60">格式</td>
        <td>
            <input type="text" name="formv" id="formv" class="text" /></td>
        <td width="60" rowspan="2" valign="top"><input type="button" name="sub" id="sub" class="submit" value="确认" onclick="$('#format',parent.document).val($('#formv').val());parent.rdes.Handsontable.dataFormat.setDataFormat(nm,data,$('#formv').val());" />
            <br /><br />
            <input type="button" name="reset" id="reset" value="取消" class="reset" onclick="parent.closedialog();" /></td>
    </tr>
    <tr>
        <td>分类</td>
        <td valign="top"><label for="a"></label>
            <select name="a" size="15" multiple="multiple" id="a" onchange="sl(this.value)" style="height:260px;">
                <option value="0"  selected="selected">数值</option>
                <option value="1">货币</option>
                <option value="2">日期</option>
                <option value="3">时间</option>
                <option value="4">日期时间</option>
                <option value="5">分数</option>
                <option value="6">科学计数</option>
            </select>
            <select name="b" size="15" multiple="multiple" id="b" onchange="ck(this.value)" style="width:250px;height:260px;">
            </select></td>
    </tr>
</table>

</body>
</html>
