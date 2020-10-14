package com.sserra.mylists.business.domain.state


// Simple DataState
//sealed class DataState<out R> {
//
//    data class Success<out T>(val data: T) : DataState<T>()
//    data class Error(val exception: Exception) : DataState<Nothing>()
//    object Loading : DataState<Nothing>()
//}

// Intermediate DataState
data class DataState<T>(
    var message: Event<String>? = null,
    var loading: Boolean = false,
    var data: Event<T>? = null
)
{
    companion object {

        fun <T> error(message: String): DataState<T> {
            return DataState(message = Event(message), loading = false, data = null)
        }

        fun <T> loading(isLoading: Boolean): DataState<T> {
            return DataState(message = null, loading = isLoading, data = null)
        }

        fun <T> data(message: String? = null, data: T? = null): DataState<T> {
            return DataState(message = Event.messageEvent(message), loading = false, data = Event.dataEvent(data)
            )
        }
    }

    override fun toString(): String {
        return "DataState(message=$message,loading=$loading,data=$data)"
    }
}

// Complex DataState
//data class DataState<T>(
//    var stateMessage: StateMessage? = null,
//    var data: T? = null,
//    var stateEvent: StateEvent? = null
//) {
//
//    companion object {
//
//        fun <T> error(
//            response: Response,
//            stateEvent: StateEvent?
//        ): DataState<T> {
//            return DataState(
//                stateMessage = StateMessage(
//                    response
//                ),
//                data = null,
//                stateEvent = stateEvent
//            )
//        }
//
//        fun <T> data(
//            response: Response?,
//            data: T? = null,
//            stateEvent: StateEvent?
//        ): DataState<T> {
//            return DataState(
//                stateMessage = response?.let {
//                    StateMessage(
//                        it
//                    )
//                },
//                data = data,
//                stateEvent = stateEvent
//            )
//        }
//    }
//}