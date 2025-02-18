import br.com.dio.model.Board;
import br.com.dio.model.Space;
import br.com.dio.util.BoardTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;
    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {
        final var positions = Stream.of(args)
                .collect(Collectors.toMap(
                        i -> i.split(";")[0],
                        j -> j.split(";")[1]
                ));

        var option = "";

        do {
            System.out.println();
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            System.out.println("Selecione uma das opções a seguir: ");
            System.out.println("1 - Iniciar um novo jogo");
            System.out.println("2 - Colocar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - Limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");

            System.out.print("Linha: ");
            option = scanner.nextLine();

            switch (option.trim()) {
                case "1" -> startGame(positions);
                case "2" -> inputNumber();
                case "3" -> removeNumber();
                case "4" -> showCurrentGame();
                case "5" -> showGameStatus();
                case "6" -> clearGame();
                case "7" -> finishGame();
                case "8" -> System.exit(0);
                default -> System.out.println("Opção inválida");
            }

        } while (true);
    }

    private static void startGame(Map<String, String> positions) {
        if (board != null) {
            System.out.println("Jogo já foi iniciado");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();

        //
        for (int i = 0; i < BOARD_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                var position = positions.get("%s,%s".formatted(i, j));
                var expected = Integer.parseInt(position.split(",")[0]);
                var fixed = Boolean.parseBoolean(position.split(",")[1]);
                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }

        board = new Board(spaces);
        System.out.println("Board foi inicializado");
    }

    private static void finishGame() {
        if (board == null) {
            System.out.println("Jogo deve ser iniciado");
            return;
        }

        if (board.gameFinished()) {
            System.out.println("Parabéns, jogo concluído!");
            showCurrentGame();
            board = null;
        } else {
            System.out.println("Não é possível finalizar!");
            showGameStatus();
        }
    }

    private static void clearGame() {
        if (board == null) {
            System.out.println("Jogo deve ser iniciado");
            return;
        }

        System.out.println("Tem certeza que deseja reiniciar?");
        var confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("s")) {
            board.reset();
            System.out.println("Reset realizado");
            return;
        }

        System.out.println("Reset cancelado");
    }

    private static void showGameStatus() {
        if (board == null) {
            System.out.println("Jogo deve ser iniciado");
            return;
        }

        System.out.printf("Status atual: %s\n", board.getStatus().toString());

        if (board.hasErrors())
            System.out.println("O jogo contém erros");
        else
            System.out.println("Jogo sem erros");
    }

    private static void showCurrentGame() {
        if (board == null) {
            System.out.println("Jogo deve ser iniciado");
            return;
        }

        var args = new Object[81];
        var argPos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (var col : board.getSpaces()) {
                var currentValue = col.get(i).getCurrent();
                args[argPos++] = " " + (currentValue != null ? currentValue : " ");
            }
        }

        System.out.println("Seu jogo se encontra da seguinte forma");
        System.out.printf((BoardTemplate.BOARD_TEMPLATE) + "%n", args);
    }

    private static void removeNumber() {
        if (board == null) {
            System.out.println("Jogo deve ser iniciado");
            return;
        }

        int min = 0;
        int max = 8;

        System.out.print("Linha - ");
        var row = getValidInt(min, max);

        System.out.print("Coluna - ");
        var column = getValidInt(min, max);

        if (!board.clearValue(column, row)){
            System.out.println("A posição informada possui um valor fixo!");
            return;
        }

        System.out.println("Número removido!");
    }

    private static void inputNumber() {
        if (board == null) {
            System.out.println("Jogo deve ser iniciado");
            return;
        }

        int min = 0;
        int max = 8;

        System.out.print("Linha - ");
        var row = getValidInt(min, max);

        System.out.print("Coluna - ");
        var column = getValidInt(min, max);

        System.out.printf("Informe o valor para a posição [%d, %d] - ", row, column);
        var value = getValidInt(1, 9);

        if (!board.changeValue(column, row, value)) {
            System.out.println("A posição informada possui um valor fixo!");
        }
    }

    private static int getValidInt(int min, int max) {
        do {
            try {
                System.out.printf("Informe um número (min %d, max %d): ", min, max);
                var retorno = scanner.nextInt();

                if (retorno < min || retorno > max)
                    throw new Exception();

                scanner.nextLine();
                return retorno;
            } catch (Exception e) {
                System.out.println("Número inválido!");
            }
        } while (true);
    }
}
