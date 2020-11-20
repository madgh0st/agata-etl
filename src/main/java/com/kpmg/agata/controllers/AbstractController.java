package com.kpmg.agata.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.kpmg.agata.constant.LogConstants.END_OF;
import static com.kpmg.agata.constant.LogConstants.STAGE;
import static com.kpmg.agata.constant.LogConstants.START_OF;

public abstract class AbstractController {
    private static final Logger log = LoggerFactory.getLogger(AbstractController.class);
    private final Class<? extends AbstractController> extenderClass;

    public AbstractController() {
        this.extenderClass = this.getClass();
    }

    public void start() {

        log.info("{}{}: {}", START_OF, STAGE, extenderClass.getSimpleName());
        startAction();
        log.info("{}{}: {}", END_OF, STAGE, extenderClass.getSimpleName());
    }

    protected abstract void startAction();
}
