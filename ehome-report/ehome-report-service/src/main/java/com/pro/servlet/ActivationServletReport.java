package com.pro.servlet;

import com.pro.efs.Base64Utils;
import com.pro.efs.RSAUtils;
import com.pro.encryption.entity.AuthFile;
import com.pro.encryption.entrance.report.AuthUtil;
import com.pro.encryption.entrance.report.ReportEntrance;
import com.pro.log.Logger;
import com.pro.util.DateUtil;
import com.pro.util.SequenceUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

public class ActivationServletReport extends HttpServlet
{
  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.setContentType("text/html;charset=UTF-8");
    request.setCharacterEncoding("UTF-8");
    boolean creataHtmlFlag = true;
    String handlerType = "";
    String authMessage = getPararm(request, "authMessage");
    if ((authMessage == null) || ("".equals(authMessage.trim()))) {
      String activationNo = getPararm(request, "activationNo");
      if ((activationNo == null) || ("".equals(activationNo))) {
        String initValue = getPararm(request, "initValue");
        if (initValue != null)
          createHtml(response, handlerType, "激活码不能为空.");
        else
          createHtml(response, handlerType, "");
      }
      else {
        handlerType = getPararm(request, "handlerType");
        activationNo = activationNo.replaceAll(" ", "\r\n").trim();
        try {
          byte[] repActivations = Base64Utils.decode(activationNo);
          byte[] decryptData = RSAUtils.decryptByPublicKey(repActivations, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKLKNffC6L5/GlEOBtpoL51e5eXniw6TA4cEMQcBbAdEt8wAhsx5edUeWWi3M686vkgb6Q7rzLbZLXr+Ix+o3YWmNsCRIVa2rWf0L0qD5lkTyLCObqNtls/VhEC6GF0M9fFsIFv/ZTaEshjoHCZPD6oYCO3k1dtjujHCuBRpA/2wIDAQAB");
          String decryptStr = new String(decryptData);
          AuthFile authFile = (AuthFile)JSONObject.toBean(JSONObject.fromObject(decryptStr), AuthFile.class);
          if ((authFile != null) && (authFile.getAuthMacCode().equals(SequenceUtil.getSN()))) {
            if (DateUtil.isBetween(authFile.getAuthDateTime(), authFile.getAuthEndDate())) {
              AuthUtil.newAuthLockFile(authFile);
              createHtml(response, handlerType, "激活成功.");
            } else {
              createHtml(response, handlerType, "激活码失效.");
            }
          }
          else createHtml(response, handlerType, "激活失败,机器码不匹配."); 
        }
        catch (Exception e)
        {
          e.printStackTrace();
          Logger.error(e.getMessage());
          createHtml(response, handlerType, "激活失败.");
        }
      }
      creataHtmlFlag = false;
    }
    else {
      getGent(response);
    }
  }

  public static void createHtml(HttpServletResponse response, String handlerType, String msg) throws IOException {
    PrintWriter out = response.getWriter();
    out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
    out.println("<HTML>");
    out.println("  <HEAD>");
    out.println("  \t\t<TITLE>授权码激活</TITLE>");
    out.println("  \t\t<style type=\"text/css\">");
    out.println("  \t\t   .context{ width: 565px; height: 260px;background-color: #F1F1F2; }");
    out.println("  \t\t   .left_list{ width: 18%; float: left;height: 260px; background: #354045}");
    out.println("  \t\t   .right_show{ width: 82%; float: left;}");
    out.println("  \t\t   ul li.menu { color: #F2F2F2; font-size: 12px; cursor: pointer; height: 30px; line-height: 30px; text-align: center; width: 100%; float: left;  background: #354045;}");
    out.println("  \t\t   .removes { border-left: 4px solid #1aa774; background: #F1F1F2 !important; color: #000 !important;}");
    out.println("  \t\t   .hiden {display: none;}");
    out.println("  \t\t   .mac_code lable{ display:block; width:100%; height:30px;white-space:nowrap;}");
    out.println("  \t\t   .text_show{ width:290px; height:36px; border:1px solid #acbcbd; background:none; padding:0 8px;font-size: 18px;}");
    out.println("  \t\t   .s_copy{width:70px;height:34px;color:#fff;letter-spacing:1px;background:#1aa774;border-bottom:1px solid #1a6348;outline:medium;*border-bottom:0;-webkit-appearance:none;-webkit-border-radius:0;}");
    out.println("  \t\t   .s_btn{width:70px;height:34px;color:#fff;letter-spacing:1px;background:#1aa774;border-bottom:1px solid #1a6348;outline:medium;*border-bottom:0;-webkit-appearance:none;-webkit-border-radius:0;margin-top: 20px;}");
    out.println("  \t\t   .tip_show{font-size: 12px;padding: 5px;display: block;border: 1px solid black;width: 90%;line-height: 20px;}");
    out.println("  \t\t</style>");
    out.println("  \t\t<script type=\"text/javascript\">");
    out.println("  \t\t\tfunction selected(elm,indexShow){");
    out.println("  \t\t\t\tvar brs = elm.parentNode.children;");
    out.println("  \t\t\t\tfor(var i=0;i<brs.length;i++){brs[i].classList.remove('removes');}");
    out.println("  \t\t\t\telm.classList.add('removes');");
    out.println("  \t\t\t\tvar mac_codes = document.getElementsByClassName(\"mac_code\");");
    out.println("  \t\t\t\tfor(var i=0;i<mac_codes.length;i++){if(i==indexShow){mac_codes[i].classList.remove('hiden');}else{mac_codes[i].classList.add('hiden');}}");
    out.println("  \t\t\t}");
    out.println("  \t\t\tfunction copy(elm){");
    out.println("  \t\t\t    var str = elm.parentNode.children[1].value.trim();");
    out.println("  \t\t\t\tvar save = function (e){");
    out.println("  \t\t\t\t\t e.clipboardData.setData('text/plain',str);//下面会说到clipboardData对象");
    out.println("  \t\t\t\t\t e.preventDefault();//阻止默认行为");
    out.println("  \t\t\t\t}");
    out.println("  \t\t\t\tdocument.addEventListener('copy',save);");
    out.println("  \t\t\t\tdocument.execCommand('copy');//使文档处于可编辑状态，否则无效}");
    out.println("  \t\t\t\talert('机器码《'+str+'》复制成功');");
    out.println("  \t\t\t}");
    out.println("  \t\t\tfunction sub_btn(elm){");
    out.println("  \t\t\t\tvar checkVal = elm.activationNo.value.trim();");
    out.println("  \t\t\t\tif(checkVal==''){alert('激活码不能为空！');}");
    out.println("  \t\t\t\telse{elm.submit()};");
    out.println("  \t\t\t}");
    out.println("  \t\t</script>");
    out.println("  </HEAD>");
    out.println("  <BODY onload=\"msg()\"  sytle=\"overflow: hidden;\">");
    out.println("    <div class=\"context\"> ");

    out.println("    <div class=\"left_list\"> ");
    out.println("    <ul style=\"list-style-type:none; padding: 5px 3px 5px 0px; margin: 0; \">");
    out.println("    \t<li class=\"menu " + ("1".equals(handlerType) ? "" : "removes") + 
      "\" onclick=\"selected(this,0)\">获取机器码</li>");
    out.println("    \t<li class=\"menu " + (!handlerType.equals("1") ? "" : "removes") + 
      "\" onclick=\"selected(this,1)\">授权码激活</li>");
    out.println("    </ul>");
    out.println("    </div>");

    out.println("    <div class=\"right_show\"> ");
    out.println("    \t<div class=\"mac_code " + ("1".equals(handlerType) ? "hiden" : "") + "\" style=\" padding: 30px 0px 30px 30px; \"> ");
    out.println("    \t\t<lable>机器码</lable><input class=\"text_show\" type=\"text\" name=\"macNo\" readonly=\"readonly\" value=\"" + 
      SequenceUtil.getSN() + 
      "\"> <input class=\"s_copy\" type=\"button\" value=\"复制\" onclick=\"copy(this)\">");
    out.println("    \t<div style=\" margin-top: 25px;\"><span class=\"tip_show\"> ");
    out.println("    \t激活操作步奏：<br> ");
    out.println("    \t&nbsp;&nbsp;&nbsp;1.点击复制按钮或手动复制机器码。 <br> ");
    out.println("    \t&nbsp;&nbsp;&nbsp;2.打开http://product.mftcc.cn/rdp 报表平台官方网站。 <br> ");
    out.println("    \t&nbsp;&nbsp;&nbsp;3.点击购在线购买，购买后按照订单页面提示发送邮件至公司邮箱。 <br> ");
    out.println("    \t&nbsp;&nbsp;&nbsp;4.审核通过后，激活码将以邮件的形式发送给您，然后将激活码填入激活页面中，点击激活。 ");
    out.println("    \t </span></div> ");
    out.println("    \t</div> ");

    out.println("    \t<div class=\"mac_code " + (!handlerType.equals("1") ? "hiden" : "") + "\" style=\"padding: 30px; \"> ");
    out.println("    \t\t<form name=\"form\" method=\"post\" action=\"ActivationServletReport\">  ");
    out.println("    \t\t\t<lable>请输入激活码</lable> <textarea name=\"activationNo\" rows=\"3\" cols=\"20\" style=\"margin: 0px; width: 395px; height: 80px; resize: none; font-size: 18px\" \"> </textarea>  <input class=\"text_show\" type=\"hidden\" name=\"\"> ");
    out.println("    \t\t\t<input type=\"hidden\" name=\"initValue\" value=\"1\"> ");
    out.println("    \t\t\t<input type=\"hidden\" name=\"handlerType\" value=\"1\"> ");
    out.println("    \t\t\t<input class=\"s_btn\" type=\"button\" onclick=\"sub_btn(this.form)\" value=\"激活\"> ");
    out.println("    \t\t</form> ");
    out.println("    \t</div> ");

    out.println("    </div>");
    out.println("    </div>");
    out.println("  \t\t<script type=\"text/javascript\">");
    out.println(" \t\t\tfunction msg() { if(" + (!"".equals(msg)) + "){alert('" + msg + "')}}");
    out.println("  \t\t</script>");
    out.println("  </BODY>");
    out.println("</HTML>");

    out.flush();
    out.close();
  }

  private static void getGent(HttpServletResponse response)
    throws IOException
  {
    PrintWriter out = response.getWriter();
    Map msgMap = ReportEntrance.getAuthMessage();
    out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
    out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
    out.println("<head><title>授权信息</title>");
    out.println("<script type=\"text/javascript\" src=\"report/lib/jquery.min.js\"></script>");
    out.println("<script type=\"text/javascript\" src=\"report/lib/layer/layer.js\"></script>");
    out.println("<link rel=\"stylesheet\" href=\"report/lib/layer/theme/default/layer.css\" />");
    out.println("<style type=\"text/css\">");
    out.println(".btn{height: 38px;line-height: 38px;padding: 0 18px;background-color: #009688;color: #fff;white-space: nowrap;text-align: center;font-size: 14px;border: none;border-radius: 2px;cursor: pointer;width: 150px;margin-bottom: 15px;}");
    out.println(".show{background: white;width: 300px;border-radius: 4px;margin: auto;-moz-box-shadow:1px 2px 12px #333333; -webkit-box-shadow:1px 2px 12px #333333; box-shadow:1px 2px 12px #333333;}");
    out.println(".title{text-align: center;padding: 20px;font-size: 16px;font-weight: 500;}");
    out.println(".content{width: 90%;margin: auto;height: 260px;}");
    out.println(".button{ text-align: center;}");
    out.println(".un_auth{color:red;}");
    out.println(".success_auth{color:green;}");
    out.println(".downJihuo {border: 1px solid #e1e2e2;padding: 1.5em 2em;margin: 0em 2em 2em 2em;color: #000;font-size: 14px;line-height: 30px;}");
    out.println(" h1, h2, h3, h4, h5, h6 {font-weight: normal;font-size: 100%;margin: 0;padding: 0;color: #000;}");
    out.println(".downZhu {color: #ff4650;}");
    out.println(".downZhu a {color: #ff4650;font-weight: bold;}");
    out.println("</style>");
    out.println("<script type=\"text/javascript\">");
    out.println("function activation(){");
    out.println("  layer.open({");
    out.println("    type: 2,");
    out.println("    title: '激活处理页面',");
    out.println("    shadeClose: true,");
    out.println("    shade: 0.8,");
    out.println("    area: ['580px', '320px'],");
    out.println("    content: ['ActivationServletReport','no']");
    out.println("  });");
    out.println(" };");
    out.println("</script>");
    out.println("</head>");
    out.println("<body style=\"min-width:1250px\">");
    out.println("<div style=\"float: left; width: 500px;z-index: -1;\">");
    out.println("\t\t<div class=\"downJihuo\">");
    out.println("\t\t\t<h4 style=\"font-weight: bold;\">报表授权步骤：</h4>");
    out.println("\t\t\t<p>① 注册并登陆，然后在线购买报表工具，下载报表工具通用版。（限时免费活动，免费获取授权码）</p>");
    out.println("\t\t\t<p>② 在报表工具中获取机器码，将机器码、订单号、用户名发送到官方邮箱(supportout@mftcc.cn)</p>");
    out.println("\t\t\t<p>③ 订单信息审核通过后，并将激活码发送至注册账号时的邮箱中</p>");
    out.println("\t\t\t<p>④ 使用\"步骤③中的激活码\"进行激活（如不激活报表展现时会有水印信息）</p>");
    out.println("\t\t\t<p>⑤ 开始使用</p>");
    out.println("\t\t\t<p style=\"color: #ff4650;\">⑥  报表授权在开发使用阶段时,可以不授权，也不影响正常使用</p>");
    out.println("\t\t\t<p style=\"color: #ff4650;\"> 报表展示会有水印存在，在部署生产系统再进行授权即可。</p>");
    out.println("\t\t\t<p class=\"downZhu\">* 注：RDP报表工具学习交流群：");
    out.println("\t\t\t\t<a target=\"_blank\" href=\"https://jq.qq.com/?_wv=1027&amp;k=5QTurbk\">608126991（点击进群）</a>");
    out.println("\t\t\t</p>");
    out.println("\t\t</div>");
    out.println("\t</div>");
    out.println("\t<div class=\"show\">");
    out.println("\t   <div class=\"title\">授权信息</div>");
    out.println("\t   <div class=\"content\">");
    out.println("\t   \t\t<div>");
    out.println("\t\t\t\t<ul style=\"line-height: 28px;\">");
    out.println("\t\t\t\t\t<li>授权名称：" + (String)msgMap.get("name") + "</li>");
    out.println("\t\t\t\t\t<li>授权版本：v" + (String)msgMap.get("version") + "</li>");
    out.println("\t\t\t\t\t<li>系&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;统：" + (String)msgMap.get("systemType") + "</li>");
    out.println("\t\t\t\t\t<li>授权类型：" + (String)msgMap.get("authType") + "</li>");
    if ("1".equals(msgMap.get("authSts")))
      out.println("\t\t\t\t\t<li>激活状态：<lable class=\"auth success_auth\">" + (String)msgMap.get("authMsg") + "</lable></li>");
    else {
      out.println("\t\t\t\t\t<li>激活状态：<lable class=\"auth un_auth\">" + (String)msgMap.get("authMsg") + "</lable></li>");
    }
    out.println("\t\t\t\t\t<li>机器码：" + (String)msgMap.get("sn") + "</li>");
    out.println("\t\t\t\t</ul>\t   \t\t");
    out.println("\t   \t\t</div>");
    out.println("\t   </div>");
    if (!"1".equals(msgMap.get("authSts"))) {
      out.println("\t   <div class=\"button\">");
      out.println("\t   \t\t<input type=\"button\" class=\"btn\" value=\"激活\" onclick=\"activation()\">");
      out.println("\t   </div>");
    }
    out.println("\t</div>");
    out.println("</body>");
    out.println("</html>");
    out.flush();
    out.close();
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doGet(request, response);
  }

  private static String getPararm(HttpServletRequest request, String key) {
    String returnStr = null;
    if (request.getParameter(key) != null)
      returnStr = request.getParameter(key);
    else if (request.getAttribute(key) != null) {
      returnStr = (String)request.getAttribute(key);
    }
    return returnStr;
  }
}

/* Location:           E:\work\apache-tomcat-7.0.751\webapps\RDP\WEB-INF\lib\dhcc.auth1.0.jar
 * Qualified Name:     com.pro.servlet.ActivationServletReport
*
 */