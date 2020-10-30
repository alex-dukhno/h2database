/*
 * Copyright 2004-2020 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (https://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.pagestore;

import org.h2.message.DbException;
import org.h2.store.InDoubtTransaction;

/**
 * Represents an in-doubt transaction (a transaction in the prepare phase).
 */
public class PageStoreInDoubtTransaction implements InDoubtTransaction {

    private final PageStore store;
    private final int sessionId;
    private final int pos;
    private final String transactionName;
    private int state;

    /**
     * Create a new in-doubt transaction info object.
     *
     * @param store the page store
     * @param sessionId the session id
     * @param pos the position
     * @param transaction the transaction name
     */
    public PageStoreInDoubtTransaction(PageStore store, int sessionId, int pos,
            String transaction) {
        this.store = store;
        this.sessionId = sessionId;
        this.pos = pos;
        this.transactionName = transaction;
        this.state = IN_DOUBT;
    }

    @Override
    public void setState(int state) {
        switch (state) {
        case COMMIT:
            store.setInDoubtTransactionState(sessionId, pos, true);
            break;
        case ROLLBACK:
            store.setInDoubtTransactionState(sessionId, pos, false);
            break;
        default:
            throw DbException.getInternalError("state="+state);
        }
        this.state = state;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public String getTransactionName() {
        return transactionName;
    }

}
