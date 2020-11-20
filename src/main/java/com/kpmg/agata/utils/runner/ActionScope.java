package com.kpmg.agata.utils.runner;

public enum ActionScope {
    /**
     * LOCAL with children. Doesn't include parents.
     */
    GLOBAL,
    LOCAL,
    /**
     * For internal terminal tasks only. Use ActionScope.LOCAL
     */
    HERE
}
