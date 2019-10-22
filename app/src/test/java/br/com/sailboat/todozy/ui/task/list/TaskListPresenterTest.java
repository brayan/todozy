package br.com.sailboat.todozy.ui.task.list;



public class TaskListPresenterTest {

    private TaskListPresenter presenter;

//    @Rule
//    public MockitoRule mockitoRule = MockitoJUnit.rule();
//
//    @Mock
//    GetTasksView getTasksView;
//
//    @Mock
//    TaskListContract.View view;
//
//    @Captor
//    ArgumentCaptor<AsyncHelper.Callback> callbackCaptor;
//
//    @Before
//    public void setUp() {
//        this.presenter = spy(new TaskListPresenter(getTasksView));
//        this.presenter.setView(view);
//    }
//
//    @Test
//    public void shouldCheckAndPerformFirstTimeConfigurationsOnResumeFirstSession() {
//        this.presenter.onResumeFirstSession();
//        verify(view).checkAndPerformFirstTimeConfigurations();
//    }
//
//    @Test
//    public void shouldCloseNotificationsOnResumeFirstSession() {
//        this.presenter.onResumeFirstSession();
//        verify(view).setAlarmUpdateTasks();
//    }
//
//    @Test
//    public void shouldLoadTasksOnPostResume() throws Exception {
//        List<ItemView> items = Arrays.asList(new TaskItemView(), new TaskItemView());
//
//        when(getTasksView.execute(any())).thenReturn(items);
//
//        this.presenter.postResume();
//
//        verify(view).launchAsync(callbackCaptor.capture());
//
//        callbackCaptor.getValue().doInBackground();
//
//        verify(getTasksView).execute(any());
//
//        // TEST SUCCESS AND FAILURE
//    }
//
//    @Test
//    public void shouldShowMetricsOnPostResume() {
//        TaskMetrics taskMetrics = new TaskMetrics();
//        this.presenter.getViewModel().setTaskMetrics(taskMetrics);
//        this.presenter.postResume();
//        verify(view).showMetrics(taskMetrics);
//    }
//
//    @Test
//    public void shouldClearTitleWhenSetMetricsOnPostResume() {
//        this.presenter.getViewModel().setTaskMetrics(new TaskMetrics());
//        this.presenter.postResume();
//        verify(view).setTitle("");
//    }
//
//    @Test
//    public void shouldShowTodozyTitleWhenThereIsNoMetricsOnPostResume() {
//        this.presenter.getViewModel().setTaskMetrics(null);
//        this.presenter.postResume();
//        verify(view).setMainTitle();
//    }
//
//    @Test
//    public void shouldHideMetricsWhenThereIsNoMetricsOnPostResume() {
//        this.presenter.getViewModel().setTaskMetrics(null);
//        this.presenter.postResume();
//        verify(view).hideMetrics();
//    }



}