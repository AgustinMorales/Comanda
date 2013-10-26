package com.company.comanda.peter.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * Resources used by the entire application.
 */
public interface Resources extends ClientBundle {
    public static final Resources INSTANCE =  GWT.create(Resources.class);

  @Source("images/banner.png")
  ImageResource banner();
  
  @Source("images/login_logo.png")
  ImageResource login_logo();
  
  @Source("comanda.css")
  @CssResource.NotStrict
  CssResource comanda_css();
}