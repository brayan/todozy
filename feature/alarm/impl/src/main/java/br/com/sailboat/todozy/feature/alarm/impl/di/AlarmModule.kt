package br.com.sailboat.todozy.feature.alarm.impl.di

import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository
import br.com.sailboat.todozy.feature.alarm.domain.usecase.DeleteAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetNextAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.SaveAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAlarmUpdatesUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAllAlarmsUseCase
import br.com.sailboat.todozy.feature.alarm.impl.data.datasource.AlarmLocalDataSource
import br.com.sailboat.todozy.feature.alarm.impl.data.datasource.AlarmLocalDataSourceSQLite
import br.com.sailboat.todozy.feature.alarm.impl.data.mapper.AlarmDataToAlarmMapper
import br.com.sailboat.todozy.feature.alarm.impl.data.mapper.AlarmToAlarmDataMapper
import br.com.sailboat.todozy.feature.alarm.impl.data.repository.AlarmRepositoryImpl
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.CancelAlarmSchedule
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.CancelAlarmScheduleUseCase
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.DeleteAlarm
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.GetAlarm
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.GetNextAlarm
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.SaveAlarm
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.ScheduleAlarm
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.ScheduleAlarmUpdates
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.ScheduleAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.ScheduleAllAlarms
import br.com.sailboat.todozy.feature.alarm.impl.presentation.mapper.AlarmToAlarmUiModelMapperImpl
import br.com.sailboat.todozy.feature.alarm.presentation.mapper.AlarmToAlarmUiModelMapper
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    factory<AlarmToAlarmUiModelMapper> { AlarmToAlarmUiModelMapperImpl(get()) }
}

private val domain = module {
    factory<ScheduleAlarmUseCase> { ScheduleAlarm(get()) }
    factory<ScheduleAllAlarmsUseCase> { ScheduleAllAlarms(get(), get(), get()) }
    factory<ScheduleAlarmUpdatesUseCase> { ScheduleAlarmUpdates(get()) }
    factory<CancelAlarmScheduleUseCase> { CancelAlarmSchedule(get()) }
    factory<SaveAlarmUseCase> { SaveAlarm(get(), get(), get()) }
    factory<GetAlarmUseCase> { GetAlarm(get()) }
    factory<DeleteAlarmUseCase> { DeleteAlarm(get(), get()) }
    factory<GetNextAlarmUseCase> { GetNextAlarm() }
}

private val data = module {
    factory<AlarmRepository> { AlarmRepositoryImpl(get(), get(), get()) }
    factory<AlarmLocalDataSource> { AlarmLocalDataSourceSQLite(get()) }
    factory { AlarmDataToAlarmMapper() }
    factory { AlarmToAlarmDataMapper() }
}

val alarmModule: List<Module> = listOf(presentation, domain, data)
