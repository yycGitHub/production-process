package com.surekam.modules.sys.security;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jasig.cas.client.session.SessionMappingStorage;
import org.jasig.cas.client.session.SingleSignOutFilter;

import com.surekam.common.utils.SpringContextHolder;
import com.surekam.modules.api.service.ApiUserService;

public final class SingleSignOutHttpSessionListener implements HttpSessionListener {

    private SessionMappingStorage sessionMappingStorage;
	private ApiUserService apiUserService;

    public void sessionCreated(final HttpSessionEvent event) {
        // nothing to do at the moment
    }

    public void sessionDestroyed(final HttpSessionEvent event) {
        if (sessionMappingStorage == null) {
            sessionMappingStorage = getSessionMappingStorage();
        }
        final HttpSession session = event.getSession();
        String agroToken = (String)session.getAttribute("Agro-Token");
        this.getApiUserService().destroyedUser(agroToken);
        sessionMappingStorage.removeBySessionById(session.getId());
    }

    /**
     * Obtains a {@link SessionMappingStorage} object. Assumes this method will always return the same
     * instance of the object.  It assumes this because it generally lazily calls the method.
     * 
     * @return the SessionMappingStorage
     */
    protected static SessionMappingStorage getSessionMappingStorage() {
        return SingleSignOutFilter.getSingleSignOutHandler().getSessionMappingStorage();
    }
    
	public ApiUserService getApiUserService() {
		if (apiUserService == null){
			apiUserService = SpringContextHolder.getBean(ApiUserService.class);
		}
		return apiUserService;
	}
}
