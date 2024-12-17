package radar.logging

import java.sql.Timestamp

interface Loggable {
    val time: Timestamp
}
