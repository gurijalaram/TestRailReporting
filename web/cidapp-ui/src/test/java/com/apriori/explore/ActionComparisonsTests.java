package com.apriori.explore;

import com.apriori.testconfig.TestBaseUI;

public class ActionComparisonsTests extends TestBaseUI {

    public ActionComparisonsTests() {
        super();
    }

    /*@Test
    @Tag(SMOKE)
    @TestRail(id = {436"})
    @Description("In comparison view, the user can assign the currently open public comparison")
    public void actionsAssignComparison() {

        String testComparisonName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .openComparison(testComparisonName);

        new GenericHeader(driver).selectAssignScenario()
            .selectAssignee("Nataliia Valieieva")
            .update(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ComparePage.class)
            .selectInfoNotes();

        assertThat(scenarioNotesPage.isAssignee(), is("Nataliia Valieieva"));
    }*/

    /*@Test
    @TestRail(id = {437"})
    @Description("In explore view, the user can assign the currently selected public comparison")
    public void actionsAssignComparisonExploreView() {

        String testComparisonName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.COMPARISONS.getWorkspace())
            .highlightComparison(testComparisonName)
            .selectAssignScenario()
            .selectAssignee("Nataliia Valieieva")
            .update(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .openComparison(testComparisonName)
            .selectInfoNotes();

        assertThat(scenarioNotesPage.isAssignee(), is("Nataliia Valieieva"));
    }*/

    /*@Test
    @TestRail(id = {438"})
    @Description("In private comparison view, the user can add Info & notes to the currently open comparison")
    public void addInfoNotesInPrivateComparisonView() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
        final ProcessGroupEnum processGroupEnum2 = ProcessGroupEnum.PLASTIC_MOLDING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "1027312-101-A1333.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testAssemblyName = "1027312-101-A1333";
        String partName = "1027311-001";

        loginPage = new CidLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Assembly")
            .setRowOne("Part Name", "Contains", testAssemblyName)
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(scenarioName, testAssemblyName)
            .apply(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", partName)
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(scenarioName, partName)
            .apply(ComparePage.class)
            .selectInfoNotes()
            .enterScenarioInfoNotesForComparison("New", "Low", "QA Test Description")
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ComparePage.class)
            .selectInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("New"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Low"), is(true));
        assertThat(scenarioNotesPage.getDescription(), is("QA Test Description"));
    }*/

    /*@Test
    @TestRail(id = {438"})
    @Description("In public comparison view, the user can add Info & notes to the currently open comparison")
    public void addInfoNotesInPublicComparisonView() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "1027312-101-A1333.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String testAssemblyName = "1027312-101-A1333";
        String partName = "1027311-001";

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Assembly")
            .setRowOne("Part Name", "Contains", testAssemblyName)
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(scenarioName, testAssemblyName)
            .apply(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", partName)
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(scenarioName, partName)
            .apply(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        comparePage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Comparison")
            .apply(ScenarioTablePage.class)
            .openComparison(testComparisonName)
            .selectInfoNotes()
            .enterScenarioInfoNotesForComparison("Analysis", "Initial", "QA Test Description")
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ComparePage.class)
            .selectInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("Analysis"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Initial"), is(true));
        assertThat(scenarioNotesPage.getDescription(), is("QA Test Description"));
    }*/

    /*@Test
    @TestRail(id = {439"})
    @Description("In explore view, the user can add Info & notes to the currently selected private comparison")
    public void addInfoNotesFofPrivateComparisonExploreView() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String partName = "PowderMetalShaft";

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", partName)
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(scenarioName, partName)
            .apply(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.selectExploreButton()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Comparison")
            .apply(ScenarioTablePage.class)
            .highlightComparison(testComparisonName)
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotesForComparison("Analysis", "Initial", "QA Test Description")
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectScenarioInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("Analysis"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Initial"), is(true));
        assertThat(scenarioNotesPage.getDescription(), is("QA Test Description"));
    }*/

    /*@Test
    @TestRail(id = {439"})
    @Description("In explore view, the user can add Info & notes to the currently selected public comparison")
    public void addInfoNotesFofComparisonExploreView() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.POWDER_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "PowderMetalShaft.stp");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String testComparisonName = new GenerateStringUtil().generateComparisonName();
        String partName = "PowderMetalShaft";

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(scenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class)
            .addScenario()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Part Name", "Contains", partName)
            .apply(ScenarioTablePage.class)
            .selectComparisonScenario(scenarioName, partName)
            .apply(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Comparison")
            .apply(ScenarioTablePage.class)
            .highlightComparison(testComparisonName)
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotesForComparison("New", "Low", "QA Test Description")
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectScenarioInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("New"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Low"), is(true));
        assertThat(scenarioNotesPage.getDescription(), is("QA Test Description"));
    }*/

    /*@Test
    @TestRail(id = {440"})
    @Description("For private comparisons, all characters should be tested in Info & notes")
    public void testAllCharactersInfoNotesPrivateComparison() {

        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.selectExploreButton()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Comparison")
            .apply(ScenarioTablePage.class)
            .highlightComparison(testComparisonName)
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotesForComparison("Analysis", "Initial", "!£$%^&()_+{}~`1-=[]#';@")
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectScenarioInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("Analysis"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Initial"), is(true));
        assertThat(scenarioNotesPage.getDescription(), is("!£$%^&()_+{}~`1-=[]#';@"));
    }*/

    /*@Test
    @TestRail(id = {440"})
    @Description("For public comparisons, all characters should be tested in Info & notes")
    public void testAllCharactersInfoNotesPublicComparison() {

        String testComparisonName = new GenerateStringUtil().generateComparisonName();

        loginPage = new CidAppLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        genericHeader = new GenericHeader(driver);
        scenarioNotesPage = genericHeader.publishScenario(PublishPage.class)
            .selectPublishButton()
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Comparison")
            .apply(ScenarioTablePage.class)
            .highlightComparison(testComparisonName)
            .selectScenarioInfoNotes()
            .enterScenarioInfoNotesForComparison("New", "Low", "!£$%^&()_+{}~`1-=[]#';@")
            .save(ExplorePage.class)
            .openJobQueue()
            .checkJobQueueActionStatus(testComparisonName, "Initial", "Update", "okay")
            .closeJobQueue(ExplorePage.class)
            .selectScenarioInfoNotes();

        assertThat(scenarioNotesPage.isStatusSelected("New"), is(true));
        assertThat(scenarioNotesPage.isCostMaturitySelected("Low"), is(true));
        assertThat(scenarioNotesPage.getDescription(), is("!£$%^&()_+{}~`1-=[]#';@"));
    }*/
}
