import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class ConversorMoeda {
    private static final String API_KEY = "ed9871ed963d1061777734de";
    private static final String URL_API = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n***************************************");
            System.out.println("            ---Bem-Vindo!---");
            System.out.println("      --- Conversor de Moedas ---");
            System.out.println("****************************************");
            System.out.println("1. Dólar (USD) para Real (BRL)");
            System.out.println("2. Real (BRL) para Dólar (USD)");
            System.out.println("3. Peso Argentino (ARS) para Real (BRL)");
            System.out.println("4. Real (BRL) para Peso Argentino (ARS)");
            System.out.println("5. Euro (EUR) para Real (BRL)");
            System.out.println("6. Real (BRL) para Euro (EUR)");
            System.out.println("7. Sair");
            System.out.println("****************************************");
            System.out.print("\nDigite o número da opção: ");

            int opcao;
            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
            } else {
                System.out.println("Por favor, digite um número válido.");
                scanner.next(); // limpa entrada inválida
                continue;
            }

            String moedaOrigem = "";
            String moedaDestino = "";

            if (opcao == 7) {
                System.out.println("Encerrando o conversor. Até logo!");
                break;
            } else if (opcao == 1) {
                moedaOrigem = "USD";
                moedaDestino = "BRL";
            } else if (opcao == 2) {
                moedaOrigem = "BRL";
                moedaDestino = "USD";
            } else if (opcao == 3) {
                moedaOrigem = "ARS";
                moedaDestino = "BRL";
            } else if (opcao == 4) {
                moedaOrigem = "BRL";
                moedaDestino = "ARS";
            } else if (opcao == 5) {
                moedaOrigem = "EUR";
                moedaDestino = "BRL";
            } else if (opcao == 6) {
                moedaOrigem = "BRL";
                moedaDestino = "EUR";
            } else {
                System.out.println("Opção inválida! Tente novamente.\n");
                continue;
            }

            System.out.print("Digite o valor a ser convertido: ");
            double valor;

            if (scanner.hasNextDouble()) {
                valor = scanner.nextDouble();
            } else {
                System.out.println("Por favor, digite um valor numérico.");
                scanner.next(); // limpa entrada inválida
                continue;
            }

            try {
                double taxaCambio = obterTaxaCambio(moedaOrigem, moedaDestino);
                double valorConvertido = valor * taxaCambio;
                System.out.printf("%.2f %s = %.2f %s%n", valor, moedaOrigem, valorConvertido, moedaDestino);
            } catch (Exception e) {
                System.err.println("Erro ao obter a taxa de câmbio: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static double obterTaxaCambio(String moedaOrigem, String moedaDestino) throws Exception {
        String url = URL_API + API_KEY + "/latest/" + moedaOrigem;
        HttpURLConnection conexao = (HttpURLConnection) new URL(url).openConnection();
        conexao.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()))) {
            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                resposta.append(linha);
            }

            JSONObject json = new JSONObject(resposta.toString());
            if (!json.getString("result").equals("success")) {
                throw new Exception("Falha na resposta da API");
            }

            JSONObject taxas = json.getJSONObject("conversion_rates");
            return taxas.getDouble(moedaDestino);
        }
    }
}

