package br.com.sailboat.todozy.feature.alarm.di

import br.com.sailboat.todozy.feature.alarm.data.datasource.AlarmLocalDataSource
import br.com.sailboat.todozy.feature.alarm.data.datasource.AlarmLocalDataSourceSQLite
import br.com.sailboat.todozy.feature.alarm.data.mapper.AlarmDataToAlarmMapper
import br.com.sailboat.todozy.feature.alarm.data.mapper.AlarmToAlarmDataMapper
import br.com.sailboat.todozy.feature.alarm.data.repository.AlarmRepositoryImpl
import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository
import br.com.sailboat.todozy.feature.alarm.domain.usecase.*
import br.com.sailboat.todozy.feature.alarm.presentation.mapper.AlarmToAlarmUiModelMapper
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    single { AlarmToAlarmUiModelMapper(get()) }
}

private val domain = module {
    factory<ScheduleAlarmUseCase> { ScheduleAlarm(get()) }
    factory<ScheduleAllAlarmsUseCase> { ScheduleAllAlarms(get(), get()) }
    factory<ScheduleAlarmUpdatesUseCase> { ScheduleAlarmUpdates(get()) }
    factory<CancelAlarmScheduleUseCase> { CancelAlarmSchedule(get()) }
    factory<SaveAlarmUseCase> { SaveAlarm(get(), get(), get()) }
}

private val data = module {
    single<AlarmRepository> { AlarmRepositoryImpl(get(), get(), get()) }
    single<AlarmLocalDataSource> { AlarmLocalDataSourceSQLite(get()) }
    single { AlarmDataToAlarmMapper() }
    single { AlarmToAlarmDataMapper() }
}

val alarmComponent: List<Module> = listOf(presentation, domain, data)