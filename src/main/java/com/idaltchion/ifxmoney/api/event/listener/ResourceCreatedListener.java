package com.idaltchion.ifxmoney.api.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.idaltchion.ifxmoney.api.event.ResourceCreatedEvent;

@Component
public class ResourceCreatedListener implements ApplicationListener<ResourceCreatedEvent> {
	
	@Override
	public void onApplicationEvent(ResourceCreatedEvent resourceCreatedEvent) {
		HttpServletResponse response = resourceCreatedEvent.getResponse();
		Long codigo = resourceCreatedEvent.getCodigo();
		
		addHeaderLocation(response, codigo);		
	}

	private void addHeaderLocation(HttpServletResponse response, Long codigo) {
		URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{codigo}")
				.buildAndExpand(codigo).toUri();
		response.setHeader("Location", uri.toASCIIString());
	}
	
}
