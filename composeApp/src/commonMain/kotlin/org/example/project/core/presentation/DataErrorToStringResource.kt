package org.example.project.core.presentation

import com.plcoding.bookpedia.core.presentation.UiText
import habitsync.composeapp.generated.resources.Res
import habitsync.composeapp.generated.resources.error_disk_full
import habitsync.composeapp.generated.resources.error_no_internet
import habitsync.composeapp.generated.resources.error_request_timeout
import habitsync.composeapp.generated.resources.error_serialization
import habitsync.composeapp.generated.resources.error_too_many_requests
import habitsync.composeapp.generated.resources.error_unknown

import org.example.project.core.domain.DataError


fun DataError.toUiText(): UiText {
    val stringRes = when(this) {
        DataError.Local.DISK_FULL -> Res.string.error_disk_full
        DataError.Local.UNKNOWN -> Res.string.error_unknown
        DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
        DataError.Remote.SERVER -> Res.string.error_unknown
        DataError.Remote.SERIALIZATION -> Res.string.error_serialization
        DataError.Remote.UNKNOWN -> Res.string.error_unknown
    }
    return UiText.StringResourceId(stringRes)
}