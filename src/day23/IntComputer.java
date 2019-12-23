package day23;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

class IntComputer {

	private long[] program;
	private int position = 0;
	private int relativePosition = 0;
	private Queue<Long> inputQueue = new LinkedList<>();
	private Queue<Long> outputQueue = new LinkedList<>();

	IntComputer(String code) {
		this.program = compileCode(code);
	}

	private IntComputer(IntComputer intComputer) {
		this.program = Arrays.copyOf(intComputer.program, intComputer.program.length);
		this.position = intComputer.position;
		this.relativePosition = intComputer.relativePosition;
		this.inputQueue = new LinkedList<>(intComputer.inputQueue);
		this.outputQueue = new LinkedList<>(intComputer.outputQueue);
	}

	ExitCode processInput(long input) {
		inputQueue.add(input);
		return process();
	}

	ExitCode process() {
		while (position < program.length) {
			int operationCode = (int) program[position];
			int operation = calculateOperation(operationCode);
			int[] mode = calculateModes(operationCode);
			if (operation == 1) {
				mathOperation(Math::addExact, position, mode);
				position = position + 4;
			} else if (operation == 2) {
				mathOperation(Math::multiplyExact, position, mode);
				position = position + 4;
			} else if (operation == 3) {
				if(!useInput(position, mode)) {
					return ExitCode.WAITING_FOR_INPUT;
				}
				position += 2;
			} else if (operation == 4) {
				output(position, mode);
				position += 2;
			} else if (operation == 5) {
				position = movePositionIf(v -> v > 0, position, mode);
			} else if (operation == 6) {
				position = movePositionIf(v -> v == 0, position, mode);
			} else if (operation == 7) {
				verify((v1, v2) -> v1 < v2, position, mode);
				position += 4;
			} else if (operation == 8) {
				verify(Long::equals, position, mode);
				position += 4;
			} else if (operation == 9) {
				setRelativePosition(position, mode);
				position += 2;
			} else if (operation == 99) {
				return ExitCode.COMPLETED;
			} else {
				return ExitCode.INVALID_STATUS;
			}
		}
		return ExitCode.INVALID_STATUS;
	}


	public Queue<Long> getOutput() {
		return outputQueue;
	}

	private int calculateOperation(int operationDigit) {
		return operationDigit % 100;
	}

	private int[] calculateModes(int operationDigit) {
		int[] modes = new int[3];
		int parametersDigit = operationDigit / 100;
		for (int i = 0; i < 3; i++) {
			modes[i] = parametersDigit % 10;
			parametersDigit /= 10;
		}
		return modes;
	}

	private void mathOperation(BiFunction<Long, Long, Long> function, int operationPosition, int[] mode) {
		long inputParameter1 = getParameter(operationPosition + 1, mode[0]);
		long inputParameter2 = getParameter(operationPosition + 2, mode[1]);
		int outputParameter = getLocation(operationPosition + 3, mode[2]);
		write(function.apply(inputParameter1, inputParameter2), outputParameter);
	}

	private int movePositionIf(Predicate<Long> predicate, int operationPosition, int[] mode) {
		long parameter = getParameter(operationPosition + 1, mode[0]);
		if (predicate.test(parameter)) {
			operationPosition = (int) getParameter(operationPosition + 2, mode[1]);
		} else {
			operationPosition += 3;
		}
		return operationPosition;
	}

	private void verify(BiPredicate<Long, Long> predicate, int operationPosition, int[] mode) {
		long parameter1 = getParameter(operationPosition + 1, mode[0]);
		long parameter2 = getParameter(operationPosition + 2, mode[1]);
		int outputParameter = getLocation(position + 3, mode[2]);
		if (predicate.test(parameter1, parameter2)) {
			write(1, outputParameter);
		} else {
			write(0, outputParameter);
		}
	}

	private boolean useInput(int position, int[] mode) {
		if(inputQueue.isEmpty()) {
			return false;
		}
		long input = inputQueue.remove();
		int outputParameter = getLocation(position + 1, mode[0]);
		write(input, outputParameter);
		return true;
	}

	private void output(int operationPosition, int[] mode) {
		int location = getLocation(operationPosition + 1, mode[0]);
		this.outputQueue.add(program[location]);
	}

	private void setRelativePosition(int position, int[] mode) {
		long inputParamter = getParameter(position + 1, mode[0]);
		this.relativePosition += inputParamter;
	}

	private long getParameter(int position, int mode) {
		int location = getLocation(position, mode);
		return program[location];
	}

	private int getLocation(int position, int mode) {
		if (mode == 0) {
			return (int) program[position];
		} else if (mode == 1) {
			return position;
		} else {
			return (int) (relativePosition + program[position]);
		}
	}

	private void write(long value, int location) {
		program[location] = value;
	}

	private long[] compileCode(String code) {
		long[] program = Arrays.stream(code.split(",")).mapToLong(Long::valueOf).toArray();
		return Arrays.copyOf(program, program.length * 5);
	}

	public IntComputer copy() {
		return new IntComputer(this);
	}

	public enum ExitCode {
		COMPLETED,
		WAITING_FOR_INPUT,
		INVALID_STATUS
	}
}

