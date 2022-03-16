package br.com.sailboat.todozy.feature.alarm.impl.di

import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository
import br.com.sailboat.todozy.feature.alarm.domain.usecase.*
import br.com.sailboat.todozy.feature.alarm.impl.data.datasource.AlarmLocalDataSource
import br.com.sailboat.todozy.feature.alarm.impl.data.datasource.AlarmLocalDataSourceSQLite
import br.com.sailboat.todozy.feature.alarm.impl.data.mapper.AlarmDataToAlarmMapper
import br.com.sailboat.todozy.feature.alarm.impl.data.mapper.AlarmToAlarmDataMapper
import br.com.sailboat.todozy.feature.alarm.impl.data.repository.AlarmRepositoryImpl
import br.com.sailboat.todozy.feature.alarm.impl.domain.usecase.*
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

val alarmComponent: List<Module> = listOf(presentation, domain, data)