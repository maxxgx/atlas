package atlas.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import atlas.utils.Counter;
import atlas.utils.Units;

/**
 * This class functions as a clock if it has been provided with a valid start
 * date, otherwise it just counts the elapsed time.
 *
 */
public class SimClock implements java.io.Serializable {

	private static final long serialVersionUID = -1532724227194921810L;

	private Counter time;
	private Long epochOffset = null;
	private final SimpleDateFormat DATE_FOR = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private final DecimalFormat DEC_FOR = new DecimalFormat(".##");

	/**
	 * Default clock don't have a date, just a time counter.
	 */
	public SimClock() {
		this.time = new Counter();
	}

	/**
	 * Construct a clock which displays date based on the epoch.
	 * 
	 * @param offset
	 *            relative to UTC epoch.
	 */
	public SimClock(long offset) {
		this();
		this.epochOffset = offset;
	}

	/**
	 * Increments the time counter
	 * 
	 * @param dt
	 *            delta time(seconds) to increment
	 */
	public void update(long dt) {
		this.time.increment(dt * 1000);
	}

	/**
	 * Time from the start of the simulation.
	 * 
	 * @return amount of time without any offset
	 */
	public long currentSimTime() {
		return this.time.get();
	}

	/**
	 * Resets the time counter.
	 */
	public void resetClock() {
		this.time = new Counter();
	}

	protected Optional<Long> getEpoch() {
		return Optional.ofNullable(this.epochOffset);
	}

	/**
	 * Removes the offset form the UTC epoch.
	 */
	public void removeEpoch() {
		this.epochOffset = null;
	}

	/**
	 * This method sets a new offset form the UTC epoch
	 * 
	 * @param offset
	 */
	public void setEpoch(long offset) {
		this.epochOffset = offset;
	}

	/**
	 * Returns the date in the current String format.
	 */
	public String toString() {
		if (this.getEpoch().isPresent()) {
			return DATE_FOR.format(new Date(this.time.get() + this.getEpoch().get()));
		} else {
			double seconds = this.time.get() / 1000;
			String time;
			if (seconds > Units.YEAR_SEC.getValue()) {
				time = DEC_FOR.format((seconds /= Units.YEAR_SEC.getValue())).concat(" years");
			} else if (seconds > Units.MONTH_SEC.getValue()) {
				time = DEC_FOR.format((seconds /= Units.MONTH_SEC.getValue())).concat(" months");
			} else if (seconds > Units.WEEK_SEC.getValue()) {
				time = DEC_FOR.format((seconds /= Units.WEEK_SEC.getValue())).concat(" weeks");
			} else if (seconds > Units.DAY_SEC.getValue()) {
				time = DEC_FOR.format((seconds /= Units.DAY_SEC.getValue())).concat(" days");
			} else if (seconds > Units.HOUR_SEC.getValue()) {
				time = DEC_FOR.format((seconds /= Units.HOUR_SEC.getValue())).concat(" hours");
			} else if (seconds > Units.MIN_SEC.getValue()) {
				time = DEC_FOR.format((seconds /= Units.MIN_SEC.getValue())).concat(" minutes");
			} else {
				time = seconds + "seconds";
			}
			return time;
		}
	}
}
