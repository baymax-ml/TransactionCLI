package com.baymax.transactioncli.data.model.transaction

import org.jetbrains.annotations.TestOnly

/**
 * Core to handle transactions.
 *
 * [transactionPool] Stack to save transaction to operation BEGIN, COMMIT and ROLLBACK
 * [currentTransaction] Transaction which user operate with
 *
 * Methods: [commitTransaction], [beginTransaction], [rollbackTransaction], [countValue], [setKeyValue], [getKeyValue], [deleteKeyValue]
 * For Test: [clear]
 */
object TransactionPool {
    /**
     * Transaction Pool to save transactions in a stack
     */
    private val transactionPool: MutableList<Transaction> = mutableListOf()

    /**
     * Transaction which user operate with
     */
    private var currentTransaction: Transaction = Transaction()

    /**
     * Commit current transaction
     * If there is no transaction began, the operation fails with "no transaction" message
     * It there is any transaction began, it removes it from the transaction pool
     */
    fun commitTransaction(): TransactionResponse {
        if (transactionPool.size == 0) {
            return TransactionResponse.FAILED("no transaction")
        }
        transactionPool.removeLast()
        return TransactionResponse.SUCCESS()
    }

    /**
     * Begin a new transaction
     * Push the current transaction to the pool for Rollback later
     */
    fun beginTransaction(): TransactionResponse {
        transactionPool.add(currentTransaction.clone())
        return TransactionResponse.SUCCESS()
    }

    /**
     * Rollback the transaction
     * If there is no transaction began, the operation fails with "no transaction" message
     * If there is any transaction began, it rollback the latest transaction
     */
    fun rollbackTransaction(): TransactionResponse {
        if (transactionPool.size == 0) {
            return TransactionResponse.FAILED("no transaction")
        }
        currentTransaction = transactionPool.removeLast()
        return TransactionResponse.SUCCESS()
    }

    /**
     * @see Transaction.countValue
     */
    fun countValue(value: String) = currentTransaction.countValue(value)

    /**
     * @see Transaction.setKeyValue
     */
    fun setKeyValue(key: String, value: String) = currentTransaction.setKeyValue(key, value)

    /**
     * @see Transaction.getKeyValue
     */
    fun getKeyValue(key: String) = currentTransaction.getKeyValue(key)

    /**
     * @see Transaction.deleteKeyValue
     */
    fun deleteKeyValue(key: String) = currentTransaction.deleteKeyValue(key)

    /**
     * For test, clear the pool and current transaction before every test tarts so that tests should not affect the others
     */
    @TestOnly
    fun clear() {
        transactionPool.clear()
        currentTransaction.clear()
    }
}