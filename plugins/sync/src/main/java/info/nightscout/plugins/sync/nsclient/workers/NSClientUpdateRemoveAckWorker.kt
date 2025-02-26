package info.nightscout.plugins.sync.nsclient.workers

import android.content.Context
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import info.nightscout.core.utils.receivers.DataWorkerStorage
import info.nightscout.core.utils.worker.LoggingWorker
import info.nightscout.database.impl.AppRepository
import info.nightscout.interfaces.sync.DataSyncSelector.PairBolus
import info.nightscout.interfaces.sync.DataSyncSelector.PairBolusCalculatorResult
import info.nightscout.interfaces.sync.DataSyncSelector.PairCarbs
import info.nightscout.interfaces.sync.DataSyncSelector.PairEffectiveProfileSwitch
import info.nightscout.interfaces.sync.DataSyncSelector.PairExtendedBolus
import info.nightscout.interfaces.sync.DataSyncSelector.PairFood
import info.nightscout.interfaces.sync.DataSyncSelector.PairGlucoseValue
import info.nightscout.interfaces.sync.DataSyncSelector.PairOfflineEvent
import info.nightscout.interfaces.sync.DataSyncSelector.PairProfileSwitch
import info.nightscout.interfaces.sync.DataSyncSelector.PairTemporaryBasal
import info.nightscout.interfaces.sync.DataSyncSelector.PairTemporaryTarget
import info.nightscout.interfaces.sync.DataSyncSelector.PairTherapyEvent
import info.nightscout.interfaces.sync.DataSyncSelectorV1
import info.nightscout.plugins.sync.nsclient.acks.NSUpdateAck
import info.nightscout.rx.AapsSchedulers
import info.nightscout.rx.bus.RxBus
import info.nightscout.rx.events.EventNSClientNewLog
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class NSClientUpdateRemoveAckWorker(
    context: Context,
    params: WorkerParameters
) : LoggingWorker(context, params, Dispatchers.Default) {

    @Inject lateinit var dataWorkerStorage: DataWorkerStorage
    @Inject lateinit var repository: AppRepository
    @Inject lateinit var rxBus: RxBus
    @Inject lateinit var dataSyncSelectorV1: DataSyncSelectorV1
    @Inject lateinit var aapsSchedulers: AapsSchedulers

    override suspend fun doWorkAndLog(): Result {
        var ret = Result.success()

        val ack = dataWorkerStorage.pickupObject(inputData.getLong(DataWorkerStorage.STORE_KEY, -1)) as NSUpdateAck?
            ?: return Result.failure(workDataOf("Error" to "missing input data"))

        // new room way
        when (ack.originalObject) {
            is PairTemporaryTarget       -> {
                val pair = ack.originalObject
                dataSyncSelectorV1.confirmLastTempTargetsIdIfGreater(pair.id)
                rxBus.send(EventNSClientNewLog("◄ DBUPDATE", "Acked TemporaryTarget" + ack._id))
                // Send new if waiting
                dataSyncSelectorV1.processChangedTempTargets()
                ret = Result.success(workDataOf("ProcessedData" to pair.toString()))
            }

            is PairGlucoseValue          -> {
                val pair = ack.originalObject
                dataSyncSelectorV1.confirmLastGlucoseValueIdIfGreater(pair.id)
                rxBus.send(EventNSClientNewLog("◄ DBUPDATE", "Acked GlucoseValue " + ack._id))
                // Send new if waiting
                dataSyncSelectorV1.processChangedGlucoseValues()
                ret = Result.success(workDataOf("ProcessedData" to pair.toString()))
            }

            is PairFood                  -> {
                val pair = ack.originalObject
                dataSyncSelectorV1.confirmLastFoodIdIfGreater(pair.id)
                rxBus.send(EventNSClientNewLog("◄ DBUPDATE", "Acked Food " + ack._id))
                // Send new if waiting
                dataSyncSelectorV1.processChangedFoods()
                ret = Result.success(workDataOf("ProcessedData" to pair.toString()))
            }

            is PairTherapyEvent          -> {
                val pair = ack.originalObject
                dataSyncSelectorV1.confirmLastTherapyEventIdIfGreater(pair.id)
                rxBus.send(EventNSClientNewLog("◄ DBUPDATE", "Acked TherapyEvent " + ack._id))
                // Send new if waiting
                dataSyncSelectorV1.processChangedTherapyEvents()
                ret = Result.success(workDataOf("ProcessedData" to pair.toString()))
            }

            is PairBolus                 -> {
                val pair = ack.originalObject
                dataSyncSelectorV1.confirmLastBolusIdIfGreater(pair.id)
                rxBus.send(EventNSClientNewLog("◄ DBUPDATE", "Acked Bolus " + ack._id))
                // Send new if waiting
                dataSyncSelectorV1.processChangedBoluses()
                ret = Result.success(workDataOf("ProcessedData" to pair.toString()))
            }

            is PairCarbs                 -> {
                val pair = ack.originalObject
                dataSyncSelectorV1.confirmLastCarbsIdIfGreater(pair.id)
                rxBus.send(EventNSClientNewLog("◄ DBUPDATE", "Acked Carbs " + ack._id))
                // Send new if waiting
                dataSyncSelectorV1.processChangedCarbs()
                ret = Result.success(workDataOf("ProcessedData" to pair.toString()))
            }

            is PairBolusCalculatorResult -> {
                val pair = ack.originalObject
                dataSyncSelectorV1.confirmLastBolusCalculatorResultsIdIfGreater(pair.id)
                rxBus.send(EventNSClientNewLog("◄ DBUPDATE", "Acked BolusCalculatorResult " + ack._id))
                // Send new if waiting
                dataSyncSelectorV1.processChangedBolusCalculatorResults()
                ret = Result.success(workDataOf("ProcessedData" to pair.toString()))
            }

            is PairTemporaryBasal        -> {
                val pair = ack.originalObject
                dataSyncSelectorV1.confirmLastTemporaryBasalIdIfGreater(pair.id)
                rxBus.send(EventNSClientNewLog("◄ DBUPDATE", "Acked TemporaryBasal " + ack._id))
                // Send new if waiting
                dataSyncSelectorV1.processChangedTemporaryBasals()
                ret = Result.success(workDataOf("ProcessedData" to pair.toString()))
            }

            is PairExtendedBolus         -> {
                val pair = ack.originalObject
                dataSyncSelectorV1.confirmLastExtendedBolusIdIfGreater(pair.id)
                rxBus.send(EventNSClientNewLog("◄ DBUPDATE", "Acked ExtendedBolus " + ack._id))
                // Send new if waiting
                dataSyncSelectorV1.processChangedExtendedBoluses()
                ret = Result.success(workDataOf("ProcessedData" to pair.toString()))
            }

            is PairProfileSwitch         -> {
                val pair = ack.originalObject
                dataSyncSelectorV1.confirmLastProfileSwitchIdIfGreater(pair.id)
                rxBus.send(EventNSClientNewLog("◄ DBUPDATE", "Acked ProfileSwitch " + ack._id))
                // Send new if waiting
                dataSyncSelectorV1.processChangedProfileSwitches()
                ret = Result.success(workDataOf("ProcessedData" to pair.toString()))
            }

            is PairEffectiveProfileSwitch         -> {
                val pair = ack.originalObject
                dataSyncSelectorV1.confirmLastEffectiveProfileSwitchIdIfGreater(pair.id)
                rxBus.send(EventNSClientNewLog("◄ DBUPDATE", "Acked EffectiveProfileSwitch " + ack._id))
                // Send new if waiting
                dataSyncSelectorV1.processChangedEffectiveProfileSwitches()
                ret = Result.success(workDataOf("ProcessedData" to pair.toString()))
            }

            is PairOfflineEvent       -> {
                val pair = ack.originalObject
                dataSyncSelectorV1.confirmLastOfflineEventIdIfGreater(pair.id)
                rxBus.send(EventNSClientNewLog("◄ DBUPDATE", "Acked OfflineEvent" + ack._id))
                // Send new if waiting
                dataSyncSelectorV1.processChangedOfflineEvents()
                ret = Result.success(workDataOf("ProcessedData" to pair.toString()))
            }
        }
        return ret
    }
}