package com.ledgercore;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Base64;
import java.util.Locale;

/**
 * Sistema bancário em JavaFX - 100% código Java (sem FXML).
 *
 * Estrutura do arquivo:
 *   1. Modelo de dados (Transacao)
 *   2. Ciclo de vida da aplicação (start / main)
 *   3. Navegação entre telas (mostrarTela)
 *   4. Construção de cada tela (Login, Dashboard, Telas de serviço)
 *   5. Componentes reutilizáveis (helpers)
 *   6. Estilo (CSS embutido como String)
 *
 * Requer Java 17+ e JavaFX 17+.
 */
public class LedgerCoreApp extends Application {

    // ---------------------------------------------------------------
    // 1. MODELO DE DADOS
    // ---------------------------------------------------------------

    /** Representa uma linha do extrato. 'record' gera construtor e getters automaticamente. */
    record Transacao(String data, String descricao, String categoria, double valor) { }

    // ---------------------------------------------------------------
    // ESTADO DA APLICAÇÃO
    // ---------------------------------------------------------------

    /** Formata números como moeda brasileira, ex.: R$ 1.234,56 */
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    /** Container raiz: a tela atual é sempre o único filho deste StackPane. */
    private final StackPane raiz = new StackPane();

    /** Saldo fictício apenas para demonstração. */
    private final double saldo = 12_847.35;

    /** Controla se o saldo está visível ou oculto. */
    private boolean saldoVisivel = true;

    /** Dados fictícios do extrato. */
    private final ObservableList<Transacao> extrato = FXCollections.observableArrayList(
            new Transacao("24/10/2026", "Supermercado Bom Preço", "Alimentação", -287.90),
            new Transacao("23/10/2026", "Salário - Empresa XYZ", "Renda", 6500.00),
            new Transacao("22/10/2026", "Pix enviado - Maria Souza", "Transferência", -150.00),
            new Transacao("21/10/2026", "Assinatura streaming", "Lazer", -39.90),
            new Transacao("20/10/2026", "Pix recebido - João Lima", "Transferência", 420.00),
            new Transacao("19/10/2026", "Conta de energia", "Moradia", -213.45),
            new Transacao("18/10/2026", "Farmácia Saúde", "Saúde", -78.30),
            new Transacao("17/10/2026", "Rendimento investimentos", "Investimentos", 96.12)
    );

    // ---------------------------------------------------------------
    // 2. CICLO DE VIDA
    // ---------------------------------------------------------------

    @Override
    public void start(Stage stage) {
        // Cria a cena apontando para o container raiz e aplica o tema escuro.
        Scene scene = new Scene(raiz, 1000, 680);
        scene.getStylesheets().add(carregarCss());

        // A primeira tela exibida é o login.
        mostrarTela(criarTelaLogin());

        stage.setTitle("Banco Digital");
        stage.setMinWidth(900);
        stage.setMinHeight(620);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // ---------------------------------------------------------------
    // 3. NAVEGAÇÃO
    // ---------------------------------------------------------------

    /**
     * Troca a tela atual. Basta substituir o conteúdo do StackPane raiz:
     * é assim que "navegamos" entre telas sem criar uma nova janela.
     */
    private void mostrarTela(Node tela) {
        raiz.getChildren().setAll(tela);
    }

    // ---------------------------------------------------------------
    // 4a. TELA DE LOGIN
    // ---------------------------------------------------------------

    private Node criarTelaLogin() {
        // Título e subtítulo
        Label logo = new Label("◆ BANCO DIGITAL");
        logo.getStyleClass().add("logo");

        Label titulo = new Label("Bem-vindo de volta");
        titulo.getStyleClass().add("titulo");

        Label subtitulo = new Label("Acesse sua conta para continuar");
        subtitulo.getStyleClass().add("subtitulo");

        // Campos de entrada
        TextField campoAgencia = new TextField();
        campoAgencia.setPromptText("0000");

        TextField campoConta = new TextField();
        campoConta.setPromptText("00000-0");

        PasswordField campoSenha = new PasswordField();
        campoSenha.setPromptText("Sua senha");

        // Botão de entrar
        Button btnEntrar = new Button("Entrar");
        btnEntrar.getStyleClass().add("botao-primario");
        btnEntrar.setMaxWidth(Double.MAX_VALUE);
        btnEntrar.setDefaultButton(true); // Enter no teclado também aciona o botão

        // EVENTO: neste exemplo o botão apenas navega para o Dashboard.
        // Num sistema real, aqui você validaria os campos e consultaria o servidor.
        btnEntrar.setOnAction(e -> mostrarTela(criarTelaDashboard()));

        // Card central que agrupa o formulário
        VBox card = new VBox(16,
                logo, titulo, subtitulo,
                rotuloCampo("Agência"), campoAgencia,
                rotuloCampo("Conta"), campoConta,
                rotuloCampo("Senha"), campoSenha,
                btnEntrar);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(36));
        card.setMaxWidth(400);
        card.setMaxHeight(Region.USE_PREF_SIZE);

        // Centraliza o card na tela
        StackPane tela = new StackPane(card);
        tela.getStyleClass().add("fundo");
        return tela;
    }

    // ---------------------------------------------------------------
    // 4b. DASHBOARD PRINCIPAL
    // ---------------------------------------------------------------

    private Node criarTelaDashboard() {
        // ----- Cabeçalho: saudação + botão sair -----
        Label saudacao = new Label("Olá, Cliente 👋");
        saudacao.getStyleClass().add("titulo");

        Label dadosConta = new Label("Ag. 0001  •  Conta 12345-6");
        dadosConta.getStyleClass().add("subtitulo");

        VBox textosCabecalho = new VBox(4, saudacao, dadosConta);

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS); // empurra o botão "Sair" para a direita

        Button btnSair = new Button("Sair");
        btnSair.getStyleClass().add("botao-secundario");
        // EVENTO: volta para a tela de login
        btnSair.setOnAction(e -> mostrarTela(criarTelaLogin()));

        HBox cabecalho = new HBox(textosCabecalho, espacador, btnSair);
        cabecalho.setAlignment(Pos.CENTER_LEFT);

        // ----- Card de saldo -----
        Label rotuloSaldo = new Label("Saldo disponível");
        rotuloSaldo.getStyleClass().add("subtitulo");

        Label valorSaldo = new Label();
        valorSaldo.getStyleClass().add("saldo");
        atualizarSaldo(valorSaldo);

        Button btnOlho = new Button("Ocultar");
        btnOlho.getStyleClass().add("botao-link");
        // EVENTO: alterna entre mostrar e esconder o saldo (não troca de tela)
        btnOlho.setOnAction(e -> {
            saldoVisivel = !saldoVisivel;
            atualizarSaldo(valorSaldo);
            btnOlho.setText(saldoVisivel ? "Ocultar" : "Mostrar");
        });

        HBox linhaRotulo = new HBox(10, rotuloSaldo, btnOlho);
        linhaRotulo.setAlignment(Pos.CENTER_LEFT);

        VBox cardSaldo = new VBox(6, linhaRotulo, valorSaldo);
        cardSaldo.getStyleClass().add("card");
        cardSaldo.setPadding(new Insets(24));

        // ----- Botões rápidos -----
        Button btnPix = criarBotaoAcao("⚡", "Transferir (Pix)");
        Button btnBoleto = criarBotaoAcao("▤", "Pagar Boleto");
        Button btnInvest = criarBotaoAcao("📈", "Investimentos");

        // EVENTOS: cada botão apenas abre a tela correspondente
        btnPix.setOnAction(e -> mostrarTela(criarTelaServico(
                "Transferir (Pix)", "Aqui ficaria o formulário de transferência via Pix.")));
        btnBoleto.setOnAction(e -> mostrarTela(criarTelaServico(
                "Pagar Boleto", "Aqui ficaria a leitura do código de barras e o pagamento.")));
        btnInvest.setOnAction(e -> mostrarTela(criarTelaServico(
                "Investimentos", "Aqui ficaria a lista de produtos e sua carteira.")));

        HBox acoesRapidas = new HBox(16, btnPix, btnBoleto, btnInvest);
        for (Button b : new Button[]{btnPix, btnBoleto, btnInvest}) {
            HBox.setHgrow(b, Priority.ALWAYS); // os três dividem a largura igualmente
            b.setMaxWidth(Double.MAX_VALUE);
        }

        // ----- Extrato (tabela) -----
        Label tituloExtrato = new Label("Últimas transações");
        tituloExtrato.getStyleClass().add("secao");

        VBox cardExtrato = new VBox(12, tituloExtrato, criarTabelaExtrato());
        cardExtrato.getStyleClass().add("card");
        cardExtrato.setPadding(new Insets(20));
        VBox.setVgrow(cardExtrato, Priority.ALWAYS); // o extrato ocupa o espaço restante

        // ----- Montagem final da tela -----
        VBox conteudo = new VBox(20, cabecalho, cardSaldo, acoesRapidas, cardExtrato);
        conteudo.setPadding(new Insets(32));

        StackPane tela = new StackPane(conteudo);
        tela.getStyleClass().add("fundo");
        return tela;
    }

    /** Monta a TableView com as colunas do extrato. */
    private TableView<Transacao> criarTabelaExtrato() {
        TableView<Transacao> tabela = new TableView<>(extrato);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabela.setPlaceholder(new Label("Nenhuma transação encontrada"));
        VBox.setVgrow(tabela, Priority.ALWAYS);

        // Cada coluna recebe uma função que extrai o texto a ser exibido de cada linha.
        TableColumn<Transacao, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().data()));
        colData.setMaxWidth(1f * Integer.MAX_VALUE * 15);

        TableColumn<Transacao, String> colDescricao = new TableColumn<>("Descrição");
        colDescricao.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().descricao()));
        colDescricao.setMaxWidth(1f * Integer.MAX_VALUE * 40);

        TableColumn<Transacao, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().categoria()));
        colCategoria.setMaxWidth(1f * Integer.MAX_VALUE * 25);

        TableColumn<Transacao, String> colValor = new TableColumn<>("Valor");
        colValor.setCellValueFactory(c -> new ReadOnlyStringWrapper(MOEDA.format(c.getValue().valor())));
        // Célula personalizada: verde para entradas, vermelho para saídas.
        colValor.setCellFactory(coluna -> new TableCell<>() {
            @Override
            protected void updateItem(String texto, boolean vazio) {
                super.updateItem(texto, vazio);
                getStyleClass().removeAll("valor-positivo", "valor-negativo");
                if (vazio || texto == null) {
                    setText(null);
                    return;
                }
                setText(texto);
                Transacao t = getTableRow().getItem();
                if (t != null) {
                    getStyleClass().add(t.valor() >= 0 ? "valor-positivo" : "valor-negativo");
                }
            }
        });
        colValor.setStyle("-fx-alignment: CENTER-RIGHT;");

        tabela.getColumns().addAll(colData, colDescricao, colCategoria, colValor);
        return tabela;
    }

    /** Atualiza o texto do saldo conforme o estado "visível/oculto". */
    private void atualizarSaldo(Label label) {
        label.setText(saldoVisivel ? MOEDA.format(saldo) : "R$ ••••••");
    }

    // ---------------------------------------------------------------
    // 4c. TELA GENÉRICA DE SERVIÇO (Pix, Boleto, Investimentos)
    // ---------------------------------------------------------------

    /**
     * Tela "em construção" reaproveitada pelos três botões rápidos.
     * Serve como ponto de partida: substitua o conteúdo pela funcionalidade real.
     */
    private Node criarTelaServico(String titulo, String descricao) {
        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("titulo");

        Label lblDescricao = new Label(descricao);
        lblDescricao.getStyleClass().add("subtitulo");
        lblDescricao.setWrapText(true);

        Button btnVoltar = new Button("← Voltar ao painel");
        btnVoltar.getStyleClass().add("botao-primario");
        // EVENTO: retorna ao Dashboard
        btnVoltar.setOnAction(e -> mostrarTela(criarTelaDashboard()));

        VBox card = new VBox(16, lblTitulo, lblDescricao, btnVoltar);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(36));
        card.setMaxWidth(480);
        card.setMaxHeight(Region.USE_PREF_SIZE);

        StackPane tela = new StackPane(card);
        tela.getStyleClass().add("fundo");
        return tela;
    }

    // ---------------------------------------------------------------
    // 5. COMPONENTES REUTILIZÁVEIS
    // ---------------------------------------------------------------

    /** Rótulo pequeno exibido acima de cada campo de formulário. */
    private Label rotuloCampo(String texto) {
        Label l = new Label(texto);
        l.getStyleClass().add("rotulo-campo");
        return l;
    }

    /** Botão grande com ícone (emoji) em cima e texto embaixo. */
    private Button criarBotaoAcao(String icone, String texto) {
        Label lblIcone = new Label(icone);
        lblIcone.getStyleClass().add("icone-acao");

        Label lblTexto = new Label(texto);
        lblTexto.getStyleClass().add("texto-acao");

        VBox conteudo = new VBox(8, lblIcone, lblTexto);
        conteudo.setAlignment(Pos.CENTER);

        Button botao = new Button();
        botao.setGraphic(conteudo);
        botao.getStyleClass().add("botao-acao");
        botao.setPrefHeight(100);
        return botao;
    }

    // ---------------------------------------------------------------
    // 6. ESTILO (CSS)
    // ---------------------------------------------------------------

    /**
     * Como não usamos arquivos externos, o CSS vive numa String e é entregue
     * ao JavaFX como um "data URI" (texto codificado em Base64).
     */
    private String carregarCss() {
        String css = """
            /* ===== Paleta: fundo #0e1015 | card #171a21 | borda #262b36 | destaque #7c5cff ===== */

            .fundo {
                -fx-background-color: linear-gradient(to bottom right, #0e1015, #12151c);
            }

            .card {
                -fx-background-color: #171a21;
                -fx-background-radius: 16;
                -fx-border-color: #262b36;
                -fx-border-radius: 16;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 24, 0.1, 0, 8);
            }

            /* ----- Textos ----- */
            .label { -fx-text-fill: #e6e8ee; -fx-font-family: "Segoe UI", "Helvetica Neue", Arial; }
            .logo { -fx-text-fill: #7c5cff; -fx-font-size: 13px; -fx-font-weight: bold; -fx-letter-spacing: 2; }
            .titulo { -fx-font-size: 24px; -fx-font-weight: bold; }
            .subtitulo { -fx-text-fill: #8b92a5; -fx-font-size: 13px; }
            .secao { -fx-font-size: 16px; -fx-font-weight: bold; }
            .rotulo-campo { -fx-text-fill: #8b92a5; -fx-font-size: 12px; }
            .saldo { -fx-font-size: 38px; -fx-font-weight: bold; }

            /* ----- Campos de formulário ----- */
            .text-field, .password-field {
                -fx-background-color: #0e1015;
                -fx-text-fill: #e6e8ee;
                -fx-prompt-text-fill: #555c70;
                -fx-border-color: #262b36;
                -fx-border-radius: 10;
                -fx-background-radius: 10;
                -fx-padding: 11 14 11 14;
                -fx-font-size: 14px;
            }
            .text-field:focused, .password-field:focused { -fx-border-color: #7c5cff; }

            /* ----- Botões ----- */
            .button { -fx-cursor: hand; -fx-font-family: "Segoe UI", "Helvetica Neue", Arial; }

            .botao-primario {
                -fx-background-color: #7c5cff;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-padding: 12 20 12 20;
            }
            .botao-primario:hover { -fx-background-color: #8d72ff; }

            .botao-secundario {
                -fx-background-color: transparent;
                -fx-text-fill: #e6e8ee;
                -fx-border-color: #262b36;
                -fx-border-radius: 10;
                -fx-background-radius: 10;
                -fx-padding: 8 18 8 18;
            }
            .botao-secundario:hover { -fx-background-color: #1e222b; }

            .botao-link {
                -fx-background-color: transparent;
                -fx-text-fill: #7c5cff;
                -fx-padding: 0 4 0 4;
            }
            .botao-link:hover { -fx-text-fill: #a08bff; }

            .botao-acao {
                -fx-background-color: #171a21;
                -fx-border-color: #262b36;
                -fx-border-radius: 14;
                -fx-background-radius: 14;
            }
            .botao-acao:hover { -fx-background-color: #1e222b; -fx-border-color: #7c5cff; }
            .icone-acao { -fx-font-size: 24px; }
            .texto-acao { -fx-font-size: 13px; -fx-font-weight: bold; }

            /* ----- Tabela ----- */
            .table-view {
                -fx-background-color: transparent;
                -fx-control-inner-background: #171a21;
                -fx-table-cell-border-color: transparent;
                -fx-padding: 0;
            }
            .table-view .column-header-background { -fx-background-color: #171a21; }
            .table-view .column-header, .table-view .filler {
                -fx-background-color: #171a21;
                -fx-border-color: transparent transparent #262b36 transparent;
                -fx-size: 36;
            }
            .table-view .column-header .label { -fx-text-fill: #8b92a5; -fx-font-size: 12px; }
            .table-row-cell { -fx-background-color: transparent; -fx-cell-size: 42; }
            .table-row-cell:odd { -fx-background-color: #1a1e26; }
            .table-row-cell:hover { -fx-background-color: #222733; }
            .table-row-cell:selected { -fx-background-color: #2a2f45; }
            .table-cell { -fx-text-fill: #e6e8ee; -fx-alignment: CENTER-LEFT; }
            .table-view .placeholder .label { -fx-text-fill: #8b92a5; }
            .valor-positivo { -fx-text-fill: #34d399; -fx-font-weight: bold; }
            .valor-negativo { -fx-text-fill: #f87171; -fx-font-weight: bold; }

            /* ----- Barras de rolagem discretas ----- */
            .scroll-bar { -fx-background-color: transparent; }
            .scroll-bar .thumb { -fx-background-color: #2b3040; -fx-background-radius: 6; }
            .scroll-bar .increment-button, .scroll-bar .decrement-button { -fx-padding: 0; }
            .scroll-bar .increment-arrow, .scroll-bar .decrement-arrow { -fx-padding: 0; }
            """;
        return "data:text/css;base64,"
                + Base64.getEncoder().encodeToString(css.getBytes(StandardCharsets.UTF_8));
    }
}