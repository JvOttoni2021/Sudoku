package br.com.dio.model;

import br.com.dio.model.enums.GameStatusEnum;

import java.util.Collection;
import java.util.List;

public class Board {
    private final List<List<Space>> spaces;

    public Board(List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public boolean hasErrors() {
        if (getStatus() == GameStatusEnum.NON_STARTED) return false;

        return spaces.stream().flatMap(Collection::stream)
                .anyMatch( x -> x.getCurrent() != null
                        && x.getCurrent() != x.getExpected() );
    }

    public boolean changeValue(int column, int row, Integer value) {
        Space space = spaces.get(row).get(column);
        space.setCurrent(value);
        return !space.isFixed();
    }

    public boolean clearValue(int column, int row) {
        Space space = spaces.get(row).get(column);
        space.clearSpace();
        return !space.isFixed();
    }

    public void reset() {
        spaces.forEach(row -> row.forEach(Space::clearSpace));
    }

    public boolean gameFinished() {
        return !this.hasErrors() && this.getStatus() == GameStatusEnum.COMPLETE;
    }

    private GameStatusEnum getStatus() {
        if (spaces.stream().flatMap(Collection::stream).noneMatch(x -> !x.isFixed() && x.getCurrent() != null)) {
            return GameStatusEnum.NON_STARTED;
        }

        boolean allSpacesFiled = spaces.stream()
                .flatMap(Collection::stream)
                .noneMatch(x -> x.getCurrent() == null);

        return allSpacesFiled ? GameStatusEnum.COMPLETE : GameStatusEnum.STARTED;
    }

}
