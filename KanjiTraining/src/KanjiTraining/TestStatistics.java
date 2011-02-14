/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package KanjiTraining;

import java.util.Calendar;

/**
 *
 * @author mlalevic
 */
public class TestStatistics {
	private Calendar start;
	private int count;
	private int correct;
	private int hinted;
	public Calendar getStart() {
		return start;
	}
	public int getCount() {
		return count;
	}
	public int getCorrect() {
		return correct;
	}
	public int getHinted() {
		return hinted;
	}

	public TestStatistics(){
		count = 0;
		correct = 0;
		hinted = 0;
		start = Calendar.getInstance();
	}

	public void Correct(){
		count ++;
		correct ++;
	}

	public void Incorrect(){
		count ++;
	}

	public void Hinted(){
		hinted ++;
		count ++;
	}
	/*public int something(){
		return start. - Calendar.getInstance();
	}*/
}

