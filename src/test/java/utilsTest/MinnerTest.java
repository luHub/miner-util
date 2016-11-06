package utilsTest;

import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.scm.GitRepository;
import br.com.metricminer2.scm.commitrange.Commits;
import core.MinerVisitorStudyPart1AndPart2;
import core.MinerVisitorStudyPart3;
import miner_pojos.CommitInfo;
import org.junit.Test;
import utils.TableOutput;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * Created by luHub on 9/26/2016.
 * Functional tests for Part 3
 */
public class MinnerTest {

    //This path is not part of the project change to run this properly
    private final String repositoryFilePath="E:\\MiningRepositories\\lune\\lucene-solr";
    private final String specificPath="lucene/core/src/java";
    private final Calendar fromDate = new GregorianCalendar(2013, Calendar.JANUARY, 01);
    private final Calendar toDate = new GregorianCalendar(2013, Calendar.DECEMBER, 31);

    @Test
    public void MinnerPart1Test() throws IOException {
        MinerStudyPart1And2 minerStudyPart1And2 = new MinerStudyPart1And2();
        minerStudyPart1And2.initalize();
        minerStudyPart1And2.execute();
        Map<String, CommitInfo> commitInfoMap = minerStudyPart1And2.getMinerVisitorStudyPart1AndPart2().getCommitInfoMap();
        TableOutput.createCSVFromCommitInfo("qualityTable.csv", commitInfoMap);
    }

    /**
     * Created by luHub on 9/26/2016.
     * Functional tests for Part 3
     */
    @Test
    public void MinnerPart3(){
        MinerStudy minerStudyPart3 = new MinerStudy();
        minerStudyPart3.initalize();
        minerStudyPart3.execute();
        List<MinerVisitorStudyPart3.InductedBugMetrics> ListOfInducedBugs = minerStudyPart3.getMinerVisitorStudyPart3().getInductedBugMetricsList(); //minerVisitorStudyPart3.getInductedBugMetricsList();
    }



    //Inner class to run the test
    //TODO instantiate objects like a in a more redable way please at the beggining of the test
    public class MinerStudyPart1And2 implements Study {

        private MinerVisitorStudyPart1AndPart2 minerVisitorStudyPart1AndPart2 = new MinerVisitorStudyPart1AndPart2(specificPath);

        public  void initalize(){
            new MetricMiner2().start(new MinerStudyPart1And2());
        }

        @Override
        public void execute() {
            new RepositoryMining()
                    .in(GitRepository.singleProject(repositoryFilePath))
                    .through(Commits.betweenDates(fromDate,
                            toDate))
                    .startingFromTheBeginning()
                    .process(minerVisitorStudyPart1AndPart2)
                    .mine();
        }

        public MinerVisitorStudyPart1AndPart2 getMinerVisitorStudyPart1AndPart2() {
            return minerVisitorStudyPart1AndPart2;
        }
    }

    //Inner class to run the test
    //TODO instantiate objects like a in a more redable way please at the beggining of the test
    public class MinerStudy implements Study {

        private MinerVisitorStudyPart3 minerVisitorStudyPart3 = new MinerVisitorStudyPart3(specificPath);

        public  void initalize(){
            new MetricMiner2().start(new MinerStudy());
        }

        @Override
        public void execute() {
            new RepositoryMining()
                    .in(GitRepository.singleProject(repositoryFilePath))
                    .through(Commits.betweenDates(fromDate,
                            toDate))
                    .startingFromTheBeginning()
                    .process(minerVisitorStudyPart3)
                    .mine();
        }

        public MinerVisitorStudyPart3 getMinerVisitorStudyPart3() {
            return minerVisitorStudyPart3;
        }

        public void setMinerVisitorStudyPart3(MinerVisitorStudyPart3 minerVisitorStudyPart3) {
            this.minerVisitorStudyPart3 = minerVisitorStudyPart3;
        }
    }
}

