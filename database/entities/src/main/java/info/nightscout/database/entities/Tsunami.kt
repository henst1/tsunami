package info.nightscout.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import info.nightscout.database.entities.embedments.InterfaceIDs
import info.nightscout.database.entities.interfaces.DBEntryWithTimeAndDuration
import info.nightscout.database.entities.interfaces.TraceableDBEntry
import java.util.TimeZone

@Entity(
    tableName = TABLE_TSUNAMI,
    foreignKeys = [ForeignKey(
        entity = Tsunami::class,
        parentColumns = ["id"],
        childColumns = ["referenceId"]
    )],
    indices = [
        Index("id"),
        Index("isValid"),
        Index("nightscoutId"),
        Index("referenceId"),
        Index("timestamp")
    ]
)
data class Tsunami(
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0,
    override var version: Int = 0,
    override var dateCreated: Long = -1,
    override var isValid: Boolean = true,
    override var referenceId: Long? = null,
    @Embedded
    override var interfaceIDs_backing: InterfaceIDs? = InterfaceIDs(),
    override var timestamp: Long,
    override var utcOffset: Long = TimeZone.getDefault().getOffset(timestamp).toLong(),
    //var reason: Reason,
    //var highTarget: Double, // in mgdl
    //var lowTarget: Double, // in mgdl
    override var duration: Long, // in millis
    var tsunamiMode: Int
) : TraceableDBEntry, DBEntryWithTimeAndDuration {

    fun contentEqualsTo(other: Tsunami): Boolean =
        timestamp == other.timestamp &&
            utcOffset == other.utcOffset &&
            //reason == other.reason &&
            //highTarget == other.highTarget &&
            //lowTarget == other.lowTarget &&
            duration == other.duration &&
            isValid == other.isValid

    //fun isRecordDeleted(other: Tsunami): Boolean =
    //    isValid && !other.isValid

    fun onlyNsIdAdded(previous: Tsunami): Boolean =
        previous.id != id &&
            contentEqualsTo(previous) &&
            previous.interfaceIDs.nightscoutId == null &&
            interfaceIDs.nightscoutId != null
/*
    enum class Reason(val text: String) {
        @SerializedName("Custom")
        CUSTOM("Custom"),
        @SerializedName("Hypo")
        HYPOGLYCEMIA("Hypo"),
        @SerializedName("Activity")
        ACTIVITY("Activity"),
        @SerializedName("Eating Soon")
        EATING_SOON("Eating Soon"),
        @SerializedName("Automation")
        AUTOMATION("Automation"),
        @SerializedName("Wear")
        WEAR("Wear")
        ;

        companion object {
            fun fromString(reason: String?) = values().firstOrNull { it.text == reason } ?: CUSTOM
        }
    }*/
}