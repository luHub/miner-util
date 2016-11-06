package miner_pojos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ch.uzh.ifi.seal.changedistiller.model.classifiers.ChangeType;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.java.JavaEntityType;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import core.MethodBugLevelCollector.FileInfoHelper;
import core.MethodBugLevelCollector.UpdatesForCommits;

public class ChangeMetric {



	private int methodHistories;
	private int authors;
	private int sumOfStmtAdded;
	private int maxStmtAdded;
	private double avgStmtAdded;
	private int sumOfStmtDeleted;
	private int maxStmtDeleted;
	private double avgStmtDeleted;
	private int churn;
	private int maxChurn;
	private double avgChurn;
	private int decl;
	private int cond;
	private int elseAdded;
	private int elseDeleted;
	private int numberOfBugs;
	private List<String> commiterNameList = new ArrayList<>();
	private String methodName;

	public ChangeMetric(String methodName) {
		this.methodName = methodName;
	}

	public ChangeMetric() {
		// TODO Auto-generated constructor stub
	}

	public void increaseAuthor(String commiterName) {
		if (!this.commiterNameList.contains(commiterName)) {
			this.commiterNameList.add(commiterName);
			authors++;
		}
	}

	public void updateMetricsPerCommit(FileInfoHelper fileInfo,UpdatesForCommits updatesForCommits) {

			updateMethodHistories(fileInfo);	
			//Here we only update method History per commit
			//updateMethodHistories(fileInfo);
			// Maximum number of source code statements added to a method body
			// for
			// all method histories
			updateMaximunSourceCodeStatementsAdded(updatesForCommits.getMaxCodeStatements());
			// Maximum number of source code statements deleted from a method
			// body
			// for all method histories
			updateMaximunMethodStatementsDeleted(updatesForCommits.getMaximunMethodStatementsDeleted());
			// Maximum churn for all method histories
			updateMaximunChurn(updatesForCommits.getMaximunChurn());
			
		
	}

	private void updateMaximunSourceCodeStatementsAdded(int maxAdded) {
		if (maxAdded > this.maxStmtAdded) {
			this.maxStmtAdded = maxAdded;
		}
	}

	private void updateMaximunMethodStatementsDeleted(int maxDeleted) {
		if (maxDeleted > this.maxStmtDeleted) {
			this.maxStmtDeleted = maxDeleted;
		}
	}

	private void updateMaximunChurn(int maxChurn) {
		if (maxChurn > this.maxChurn) {
			this.maxChurn = maxChurn;
		}
	}

	public void updateMetricsPerChange(FileInfoHelper fileInfo, SourceCodeChange change) {

		if (change.getChangeType().equals(ChangeType.STATEMENT_INSERT)) {
			// Sum of all source code statements added to a method body over all
			// method histories
			this.sumOfStmtAdded++;
			// churn: Sum of stmtAdded - stmtDeleted over all method histories
			updateChurn();
			
		}
		if (change.getChangeType().equals(ChangeType.STATEMENT_DELETE)) {

			// Sum of all source code statements deleted from a method body over
			// all method histories
			this.sumOfStmtDeleted++;
			// churn: Sum of stmtAdded - stmtDeleted over all method histories
			updateChurn();
		}

		if (change.getChangeType().equals(ChangeType.RETURN_TYPE_CHANGE)
				|| change.getChangeType().equals(ChangeType.RETURN_TYPE_DELETE)
				|| change.getChangeType().equals(ChangeType.RETURN_TYPE_INSERT)
				|| change.getChangeType().equals(ChangeType.PARAMETER_ORDERING_CHANGE)
				|| change.getChangeType().equals(ChangeType.PARAMETER_TYPE_CHANGE)
				|| change.getChangeType().equals(ChangeType.PARAMETER_RENAMING)
				|| change.getChangeType().equals(ChangeType.PARAMETER_DELETE)
				|| change.getChangeType().equals(ChangeType.PARAMETER_INSERT)
				|| change.getChangeType().equals(ChangeType.METHOD_RENAMING)) {
			this.decl++;
		}

		// Number of condition expression changes in a method body over all
		// revisions
		if (change.getChangeType().equals(ChangeType.CONDITION_EXPRESSION_CHANGE)) {
			this.cond++;
		}

		// Number of added else-parts in a method body over all revisions
		if ((change.getChangeType().equals(ChangeType.STATEMENT_INSERT)
				|| change.getChangeType().equals(ChangeType.ALTERNATIVE_PART_INSERT))
				&& (change.getChangedEntity().getType().equals(JavaEntityType.ELSE_STATEMENT)
						|| change.getChangedEntity().equals(JavaEntityType.ELSE_STATEMENT))) {
			this.elseAdded++;
		}

		// Number of deleted else-parts from a method body over all revisions
		if ((change.getChangeType().equals(ChangeType.STATEMENT_DELETE)
				|| change.getChangeType().equals(ChangeType.ALTERNATIVE_PART_DELETE))
				&& (change.getChangedEntity().getType().equals(JavaEntityType.ELSE_STATEMENT)
						|| change.getChangedEntity().equals(JavaEntityType.ELSE_STATEMENT))) {
			this.elseDeleted++;
		}

	}

	private void updateAverageChurn() {
		this.avgChurn = Math.abs((double) this.churn / (double) this.methodHistories);
	}

	private void updateAverageStatementsDeleted() {
		this.avgStmtDeleted = Math.abs((double) this.sumOfStmtDeleted / (double) this.methodHistories);
	}

	private void updateAverageStatementsAdded() {
		this.avgStmtAdded = Math.abs((double) this.sumOfStmtAdded / (double) this.methodHistories);
	}

	private void updateChurn() {
		this.churn = this.sumOfStmtAdded + this.sumOfStmtDeleted;
	}

	public void updateNumberOfBugs() {
			this.numberOfBugs++;
	}

	@Override
	public String toString() {
		return methodHistories + "," + authors + "," + sumOfStmtAdded + "," + maxStmtAdded + "," + avgStmtAdded + ","
				+ sumOfStmtDeleted + "," + maxStmtDeleted + "," + avgStmtDeleted + "," + churn + "," + maxChurn + ","
				+ avgChurn + "," + decl + "," + cond + "," + elseAdded + "," + elseDeleted + "," + numberOfBugs;
	}

	public int getElseAdded() {
		return elseAdded;
	}

	public void setElseAdded(int elseAdded) {
		this.elseAdded = elseAdded;
	}

	public int getElseDeleted() {
		return elseDeleted;
	}

	public void setElseDeleted(int elseDeleted) {
		this.elseDeleted = elseDeleted;
	}

	public int getDecl() {
		return decl;
	}

	public void setDecl(int decl) {
		this.decl = decl;
	}

	public int getBugs() {
		// TODO Auto-generated method stub
		return numberOfBugs;
	}
	
	private void updateMethodHistories(FileInfoHelper fileInfo){
		increaseAuthor(fileInfo.getCommitInfo().getCommiterName());
		this.methodHistories++;
		// Average number of source code statements added to a method body
		// per
		// method history
		updateAverageStatementsAdded();

		// Average number of source code statements deleted to a method body
		// per
		// method history
		updateAverageStatementsDeleted();

		// Average churn per method history
		updateAverageChurn();
		
	}


	
}