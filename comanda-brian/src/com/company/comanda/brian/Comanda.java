package com.company.comanda.brian;

import java.util.List;

import com.company.comanda.brian.guice.BrianModule;
import com.google.inject.Module;

import roboguice.application.RoboApplication;

public class Comanda extends RoboApplication {

    @Override
    protected void addApplicationModules(List<Module> modules) {
        modules.add(new BrianModule());
    }

}
