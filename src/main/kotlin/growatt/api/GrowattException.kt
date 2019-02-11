package growatt.api

class GrowattException(val errorCode: Long, message: String? = null) : RuntimeException(message)

const val GROWATT_ERROR_CODE_OK = 0L
const val GROWATT_ERROR_CODE_SERVER_ERROR = 10001L
const val GROWATT_ERROR_CODE_USER_ID_IS_EMPTY = 10002L
const val GROWATT_ERROR_CODE_USER_NOT_FOUND = 10003L