package com.baymax.transactioncli.data.model

import com.baymax.transactioncli.data.model.transaction.TransactionPool
import com.baymax.transactioncli.data.model.transaction.TransactionResponse
import org.junit.Before
import org.junit.Test

class TransactionTest {

    @Before
    fun setup() {
        TransactionPool.clear()
    }

    @Test
    fun `set or get key value`() {
        TransactionPool.setKeyValue("abc", "123")
        val result = TransactionPool.getKeyValue("abc")
        assert(
            result == TransactionResponse.SUCCESS(
                "123"
            )
        )
    }

    @Test
    fun `return error for unset value`() {
        val result = TransactionPool.getKeyValue("xyz")
        assert(result == TransactionResponse.FAILED("key not set"))
    }

    @Test
    fun `delete existing key returns true`() {
        TransactionPool.setKeyValue("abc", "123")
        val result = TransactionPool.deleteKeyValue("abc")

        assert(result == TransactionResponse.SUCCESS())
    }

    @Test
    fun `delete unset key returns false`() {
        val result = TransactionPool.deleteKeyValue("abc")
        assert(result == TransactionResponse.FAILED("key not set"))
    }

    @Test
    fun `count keys with the value`() {
        TransactionPool.setKeyValue("abc", "123")
        TransactionPool.setKeyValue("xyz", "123")
        val result = TransactionPool.countValue("123")
        assert(result == TransactionResponse.SUCCESS("2"))
    }

    @Test
    fun `count unset value returns 0`() {
        val result = TransactionPool.countValue("123")
        assert(result == TransactionResponse.SUCCESS("0"))
    }

    @Test
    fun `commit without begin returns error` () {
        val result = TransactionPool.commitTransaction()
        assert(result == TransactionResponse.FAILED("no transaction"))
    }

    @Test
    fun `commit with begin` () {
        TransactionPool.beginTransaction()
        val result = TransactionPool.commitTransaction()
        assert(result == TransactionResponse.SUCCESS())
    }

    @Test
    fun `rollback without begin returns error`() {
        val result = TransactionPool.rollbackTransaction()
        assert(result == TransactionResponse.FAILED("no transaction"))
    }

    @Test
    fun `rollback with begin restores state` () {
        TransactionPool.setKeyValue("abc", "123")
        TransactionPool.beginTransaction()
        TransactionPool.deleteKeyValue("abc")
        TransactionPool.rollbackTransaction()
        val result = TransactionPool.getKeyValue("abc")
        assert(result == TransactionResponse.SUCCESS("123"))
    }
}
