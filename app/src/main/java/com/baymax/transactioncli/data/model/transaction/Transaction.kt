package com.baymax.transactioncli.data.model.transaction

import org.jetbrains.annotations.TestOnly

/**
 * Save value of the key in HashMap.
 * Methods: [setKeyValue], [getKeyValue], [deleteKeyValue], [countValue]
 * For Test: [clear]
 */
class Transaction(private val keyStore: HashMap<String, String> = hashMapOf()) {

    /**
     * Set the [value] to the [key]
     * If [key] or [value] is empty, the operation fails with message of its usage
     * If not, it saves the [value] to the [key]
     *
     * @param key Key to set
     * @param value Value to set
     */
    fun setKeyValue(key: String, value: String): TransactionResponse {
        if (key.isEmpty() || value.isEmpty()) {
            return TransactionResponse.FAILED("Usage: SET \$key \$value")
        }
        keyStore[key] = value
        return TransactionResponse.SUCCESS()
    }

    /**
     * Get the value of the [key]
     * If [key] is empty, the operation fails with message of its usage
     * If no value is set to the [key], the operation fails with message "key not set"
     *
     * @param key Key to get value
     */
    fun getKeyValue(key: String): TransactionResponse? {
        return if (key.isEmpty()) {
            return TransactionResponse.FAILED("Usage: GET \$key")
        } else if (!keyStore.containsKey(key)) {
            TransactionResponse.FAILED("key not set")
        } else {
            keyStore[key]?.let { TransactionResponse.SUCCESS(it) }
        }
    }

    /**
     * Delete value of the [key]
     * If [key] is empty, the operation fails with message of its usage
     * If no value is set to the [key], the operation fails with message "key not set"
     *
     * @param key Key to delete value
     */
    fun deleteKeyValue(key: String): TransactionResponse {
        if (key.isEmpty()) {
            return TransactionResponse.FAILED("Usage: DELETE \$key")
        }
        if (!keyStore.containsKey(key)) {
            return TransactionResponse.FAILED("key not set")
        } else {
            keyStore.remove(key);
            return TransactionResponse.SUCCESS()
        }
    }

    /**
     * Count the [value] in the transaction
     * If [value] is empty, the operation fails with message of its usage
     *
     * @param value Value to count in the transaction
     */
    fun countValue(value: String): TransactionResponse {
        if (value.isEmpty()) {
            return TransactionResponse.FAILED("Usage: COUNT \$value")
        }
        val count = keyStore.values.count {
            it == value
        }
        return TransactionResponse.SUCCESS("$count")
    }

    fun clone(): Transaction = Transaction(HashMap(keyStore))

    /**
     * For test, clear the Key Store before test begins so that one test should not affect the other
     */
    @TestOnly
    fun clear() {
        keyStore.clear()
    }
}