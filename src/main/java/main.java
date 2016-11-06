import java.io.IOException;
import java.util.List;

import core.MinerStudy;
import miner_pojos.FileInfo;
import table_builder.TableInfoCreator;
import utils.TableOutput;

public class main {

    /**
     * E:\MiningRepositories\lune\lucene-solr
     * Example: java main E:\MiningRepositories\lune\lucene-solr E:\MiningRepositories\lune\lucene-solr\lucene\core\src\ 2014-01-01 2015-01-01
     *
     * @param args
     */
    public static void main(String args[]) {

        MinerStudy minerStudy = new MinerStudy();
        minerStudy.initalize();

        /*
		if (args.length == 4) {
		    System.out.println("Creating Table From Git Project in Path:" + args[0]);
			System.out.println("Creating Table From Project in Path:" + args[1]);
			System.out.println("Creating Table From Project in Path Since:" + args[2]);
			System.out.println("Creating Table From Project in Path Until:" + args[3]);

			try {
				List<FileInfo>tableInfo = TableInfoCreator.createTable(args[0],args[1], args[2], args[3]);
				System.out.println("Saving Table");
				TableOutput.createResults("qualityTable.csv", tableInfo);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("Write the correct form ProjectPath Since Until");
		}
	}*/
    }
}