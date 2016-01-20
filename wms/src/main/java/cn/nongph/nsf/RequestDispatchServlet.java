package com.felix.nsf;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.felix.nsf.converter.ConverterProxy;
import com.felix.nsf.exception.SessionTimeoutException;
import com.felix.nsf.exception.UnPrivilegeException;

/**
 * 
 * @author Felix
 *
 */
@WebServlet("/RequestDispatchServlet/*")
public class RequestDispatchServlet extends HttpServlet{
	
	private static final long serialVersionUID = 3988991908579236065L;
	
	private Log log  = LogFactory.getLog( this.getClass() );
	
	private static final String EL_PREFIX  = "#{";
	
	private static final String EL_POSTFIX = "}";
	
	private static final String MULTIPART_FORM_DATA = "multipart/form-data";
	
	private String servletName;	
	
	private Set<String> APPLICATION_TYPE;
	
	private DiskFileItemFactory factory = new DiskFileItemFactory(1024*10000, null);
	
	public void init(){
		servletName = getServletConfig().getServletName();
		servletName = servletName.substring( servletName.lastIndexOf(".")+1 );
		
		APPLICATION_TYPE = new HashSet<String>();
		APPLICATION_TYPE.add( "text/xml" );
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doDispatch(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doDispatch(req, resp);
	}
	
	private void doDispatch( HttpServletRequest request, HttpServletResponse response ) throws IOException{ 
		request.setCharacterEncoding( "utf-8" );
		response.setContentType( "text/html;charset=utf-8" );
		
		String cmd = "";
		String action = "";
		String url = request.getRequestURI();
		int index = url.indexOf(servletName);
		String bAm = url.substring( index + servletName.length()+1, url.length() );
		cmd = bAm.substring(0,bAm.indexOf("/"));
		if( bAm.indexOf("?")>0 )
			action = bAm.substring( bAm.indexOf("/")+1, bAm.indexOf("?") );
		else
			action = bAm.substring( bAm.indexOf("/")+1 );
		
		String result = "";
		try {
			NsfELContext elContext = new NsfELContext( Application.getInstance().getELResolver() );
			Object beanInstance = getCommandBean( elContext, cmd );
			injectRequestParameter( beanInstance, elContext, cmd, request, response );	
			Object ro = invokeAction( beanInstance, action );
			result = JSONPackager.getInstance().package2JSONString( ro );
		} catch (InvocationTargetException e) {
			Throwable ite = e.getTargetException();
			if( ite instanceof SessionTimeoutException )
				result = "[TIME_OUT]";
			else if ( ite instanceof UnPrivilegeException )
				result = "[PERMISSION_ERROR]";
			else {
				ite.printStackTrace();
				result = "[ERROR]";
			}
		} catch (Exception e) {
			log.error( "error on dispatch request", e );
			result = "[ERROR]";
		}
	
		
		if ( result!=null ) {
			log.info(result);
			response.getWriter().write(result);
		}
	}
	
	private void injectRequestParameter( Object cmdBean, ELContext elContext, String cmd, HttpServletRequest req, HttpServletResponse rsp ) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		String ct = req.getContentType();
		log.info("Request Content Type: " + ct );
		if( ct!=null ) {
			int slipIndex = ct.indexOf(';'); 
			if( slipIndex>0 )
				ct = ct.substring(0, slipIndex);
		}
		if( ct==null || !APPLICATION_TYPE.contains( ct ) ) {
			Map<String, String[]> reqParaMap = req.getParameterMap();
			if( MULTIPART_FORM_DATA.equals( ct ) ) {
				try {
					ServletFileUpload upload = new ServletFileUpload(factory);
					List<FileItem> fileItems = upload.parseRequest(req);
					for( FileItem fi : fileItems ) {
						Object value;
						if( fi.isFormField() ) 
							value = fi.getString();
						else {
							paraMap.put( fi.getFieldName() + "Name", fi.getName() );
							value = IOUtils.toByteArray( fi.getInputStream() );
						}
						paraMap.put(fi.getFieldName(), value );

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				for( Map.Entry<String, String[]> para : reqParaMap.entrySet() ) {
					paraMap.put( para.getKey() , para.getValue()[0] );
				}
			}
		}
		
		paraMap.put( "request" , req );
		paraMap.put( "response", rsp );
		
		for( Map.Entry<String, Object> para : paraMap.entrySet() ) {
			try{
				Object value = convertRequestParameter( cmdBean, para.getKey(), para.getValue() );
				
				String exp = EL_PREFIX + cmd + '.' + para.getKey() + EL_POSTFIX;
				ValueExpression ve = Application.getInstance().getExpressionFactory().createValueExpression( elContext, exp, Object.class );
				ve.setValue( elContext, value );
			} catch ( PropertyNotFoundException e) {
				log.debug( "ignore property: " + para.getKey() );
			}
		}
	}
	
	private Object convertRequestParameter( Object action, String exp, Object value ) throws PropertyNotFoundException{
		String[] exps = exp.split( "\\." );
		Class<?> c = action.getClass();
		Field f = null;
		for( int i=0; i<exps.length; i++ ) {
			try{
				f = c.getDeclaredField( exps[i] );
				c = f.getType();
			} catch (NoSuchFieldException e){
				if( c.getSuperclass()!=null ) {
					c = c.getSuperclass();
					i--;
				} else {
					throw new PropertyNotFoundException();
				}
			}
		}
		if( value instanceof String )
			value = ConverterProxy.string2Object( f, (String)value );
		return value;
	}
	
	private Object getCommandBean( ELContext elContext, String cmd ){
		ValueExpression ve = Application.getInstance().getExpressionFactory().createValueExpression( elContext, EL_PREFIX + cmd + EL_POSTFIX, Object.class );
		return ve.getValue( elContext );
	}
	
	private Object invokeAction(Object command, String action) throws Exception {
		Method method = command.getClass().getDeclaredMethod(action);
		return method.invoke(command);
	}
}
