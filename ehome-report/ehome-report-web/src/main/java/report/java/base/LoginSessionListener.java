package report.java.base;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class LoginSessionListener
  implements HttpSessionListener
{
  public static Map<String, String> hUserName = new ConcurrentHashMap();

  public static Map<String, HttpSession> htsession = new ConcurrentHashMap();

  public static Map<String, Integer> mpOper = new ConcurrentHashMap();

  public void sessionCreated(HttpSessionEvent se)
  {
  }

  public void sessionDestroyed(HttpSessionEvent se)
  {
    hUserName.remove(se.getSession().getId());
    htsession.remove(se.getSession().getId());
    se.getSession().invalidate();
  }
  public static void putSessionMap(HttpSession session, String nUserName) {
    hUserName.put(session.getId(), nUserName);
    htsession.put(session.getId(), session);
    session.setAttribute("username", nUserName);
  }

  public static boolean kickFirstOper(HttpSession session, String nUserName)
  {
    boolean flag = false;

    if (hUserName.containsValue(nUserName)) {
      flag = true;
      Iterator iter = hUserName.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry entry = (Map.Entry)iter.next();
        Object key = entry.getKey();
        Object val = entry.getValue();
        if (((String)val).equals(nUserName)) {
          iter.remove();
          HttpSession ses = (HttpSession)htsession.get(key);
          if (session.getId().equals(((HttpSession)htsession.get(key)).getId())) {
            htsession.remove(key);
            hUserName.remove(key);

            hUserName.put(session.getId(), nUserName);
            htsession.put(session.getId(), session);
            break;
          }htsession.remove(key);
          hUserName.remove(key);
          try {
            ses.invalidate();
          } catch (Exception localException) {
          }
          hUserName.put(session.getId(), nUserName);
          htsession.put(session.getId(), session);

          break;
        }
      }
    }

    return flag;
  }

  public static boolean isAlreadyEnter(HttpSession session, String sUserName)
  {
    boolean flag = false;
    if (hUserName.containsValue(sUserName)) {
      flag = true;
      Iterator iter = hUserName.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry entry = (Map.Entry)iter.next();
        Object key = entry.getKey();
        Object val = entry.getValue();
        if (((String)val).equals(sUserName)) {
          iter.remove();
          HttpSession ses = (HttpSession)htsession.get(key);
          htsession.remove(key);
          ses.invalidate();
          break;
        }
      }
      hUserName.put(session.getId(), sUserName);
      htsession.put(session.getId(), session);
    }
    else {
      flag = false;
      hUserName.put(session.getId(), sUserName);
      htsession.put(session.getId(), session);
    }
    return flag;
  }

  public static boolean isOnline(String sUserName)
  {
    return hUserName.containsValue(sUserName);
  }

  public static boolean isOnline(HttpSession session)
  {
    return htsession.get(session.getId()) != null;
  }

  public static boolean isAddSessionToMap(HttpSession session, String sUserName)
  {
    boolean flag = false;
    if (hUserName.containsValue(sUserName)) {
      flag = true;
    } else {
      flag = false;
      hUserName.put(session.getId(), sUserName);
      htsession.put(session.getId(), session);
    }
    return flag;
  }

  public static boolean doRemove(String sUserName)
  {
    boolean flag = false;
    if (hUserName.containsValue(sUserName)) {
      flag = true;
      Iterator iter = hUserName.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry entry = (Map.Entry)iter.next();
        Object key = entry.getKey();
        Object val = entry.getValue();
        if (((String)val).equals(sUserName)) {
          HttpSession ses = (HttpSession)htsession.get(key);
          hUserName.remove(key);
          htsession.remove(key);
          ses.invalidate();
        }
      }
    }
    return flag;
  }

  public static HttpSession getSessionBySessionId(String sessionid)
  {
    HttpSession session = (HttpSession)htsession.get(sessionid);
    return session;
  }
}

/*
 * Qualified Name:     report.java.base.LoginSessionListener
*
 */