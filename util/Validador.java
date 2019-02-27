package util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe de validação sintática de dados.
 */
public final class Validador {

    public Validador() {
        //construtor privado para que a classe não seja instanciada
    }

    /**
     * Testa se uma String é nula ou está vazia.
     *
     * @param campo String a ser testada.
     * @return true se estiver vazio.
     */
    public static boolean vazio(final String campo) {

        return campo == null || campo.length() == 0;
    }

    /**
     * Testa se uma String contém um número inteiro.
     *
     * @param campo String a ser testada.
     * @return true se o conteúdo da String for um número inteiro.
     */
    public static boolean inteiro(final String campo) {

        return !Validador.vazio(campo) && campo.matches("[0-9]*");
    }

    /**
     * Testa se uma String contém um número (inteiro ou real).
     *
     * @param campo String a ser testada.
     * @return true se o conteúdo da String for um número.
     */
    public static boolean numero(final String campo) {

        return (Validador.inteiro(campo) || Validador.real(campo));
    }

    /**
     * Testa se uma String contém um número hexadecimal.
     *
     * @param campo String a ser testada.
     * @return true se o conteúdo da String for um número.
     */
    public static boolean hexa(final String campo) {

        return !Validador.vazio(campo) && campo.matches("[0-9a-fA-F]*");
    }

    /**
     * Testa se a String tem pelo menos a quandidade de bytes informada.
     *
     * @param campo String a ser testada.
     * @param qtde Quantidade de bytes que se espera encontrar no campo.
     * @return true se a String tiver pelo menos a quantidade de bytes
     * informada.
     */
    public static boolean minChars(final String campo, final int qtde) {

        return !Validador.vazio(campo) && campo.length() >= qtde;
    }

    /**
     * Testa se a String não excede a quandidade de bytes informada.
     *
     * @param campo String a ser testada.
     * @param qtde Quantidade de bytes limite que o campo pode ter.
     * @return true se a String tiver menos bytes que a quantidade informada.
     */
    public static boolean maxChars(final String campo, final int qtde) {

        return !Validador.vazio(campo) && campo.length() <= qtde;
    }

    /**
     * Testa se uma String contém um número real.
     *
     * @param campo String a ser testada.
     * @return true se o conteúdo da String for um número real.
     */
    public static boolean real(final String campo) {

        return !Validador.vazio(campo) && campo.matches("[0-9]*\\.[0-9]*");
    }

    /**
     * Testa se uma String contém somente letras.
     *
     * @param campo String a ser testada.
     * @return true se o conteúdo da String for composta somente por letras.
     */
    public static boolean alfabetico(final String campo) {

        return !Validador.vazio(campo) && campo.matches("[a-zA-Z\\s]*");
    }

    /**
     * Testa se uma String contém um email válido.
     *
     * @param campo String a ser testada.
     * @return true se o conteúdo da String contiver um email válido.
     */
    public static boolean email(final String campo) {

        String regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/="
                + "?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+"
                + "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        return !Validador.vazio(campo) && campo.matches(regexp);
    }

    /**
     * Verifica se o campo possui um formato de data válida. E necessario passar
     * a mascara a ser validado.
     *
     * @param campo String a ser validado.
     * @param mascara Formato a ser verificado.
     * @return True ou False.
     */
    public static boolean data(final String campo, final String mascara) {

        boolean retorno = false;
        if (!Validador.vazio(campo) && !Validador.vazio(mascara)) {

            try {

                SimpleDateFormat sdf = new SimpleDateFormat(mascara);
                sdf.setLenient(false);
                Date data = sdf.parse(campo);
                if (data != null) {

                    retorno = true;
                }
            } catch (Exception e) {
                // Possível ParseException
            }
        }
        return retorno;
    }

    /**
     *
     * @param string
     * @param limite
     * @return
     */
    public static String modulo11(String string, int limite) {

        int[] campo = new int[string.length()];
        int[] resultado = new int[string.length()];
        int[] multiplicador = new int[string.length()];
        int total = 0;

        int fator = 2;
        for (int i = multiplicador.length - 1; i >= 0; i--) {

            if (limite != 0 && fator > limite) {

                fator = 2;
            }
            multiplicador[i] = fator++;
        }
        for (int i = 0; i < string.length(); i++) {

            campo[i] = Integer.parseInt(string.substring(i, i + 1));
        }
        for (int i = 0; i < resultado.length; i++) {

            resultado[i] = campo[i] * multiplicador[i];
        }
        for (int i = 0; i < resultado.length; i++) {

            total += resultado[i];
        }
        int DV = 11 - (total % 11);
        return String.valueOf((DV == 10 | DV == 11) ? 0 : DV);
    }

    /**
     * Realiza a validacao do CNPJ e indica-o se e valido.
     *
     * @param cnpj
     * @return True se o CNPJ e valido.
     */
    public static boolean cnpj(String cnpj) {

        boolean retorno = false;
        if (!Validador.vazio(cnpj) || !Validador.maxChars(cnpj, 14)) {

            String nCnpj = preencherDireita(cnpj, '0', 14);
            String dv1 = modulo11(nCnpj.substring(0, 12), 9);
            String dv2 = modulo11(nCnpj.substring(0, 13), 9);
            retorno = nCnpj.substring(12).equals(dv1 + dv2);
        }
        return retorno;
    }

    private static int calcularDigito(String string, int[] peso) {

        int soma = 0;
        if (!Validador.vazio(string) && peso != null) {

            for (int indice = string.length() - 1, digito; indice >= 0; indice--) {

                digito = Integer.parseInt(string.substring(indice, indice + 1));
                soma += digito * peso[peso.length - string.length() + indice];
            }
            soma = 11 - soma % 11;
        }
        return soma > 9 ? 0 : soma;
    }

    /**
     * Realiza a validacao do CPF e indica-o se e valido.
     *
     * @param cpf
     * @return True se o CPF e valido.
     */
    public static boolean cpf(String cpf) {

        boolean retorno = false;
        if (!Validador.vazio(cpf)) {

            int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
            if (Validador.vazio(cpf) || !Validador.maxChars(cpf, 11)) {

                return false;
            }
            String nCpf = preencherDireita(cpf, '0', 11);
            Integer digito1 = calcularDigito(nCpf.substring(0, 9), pesoCPF);
            Integer digito2 = calcularDigito(nCpf.substring(0, 9) + digito1, pesoCPF);
            retorno = nCpf.equals(nCpf.substring(0, 9) + digito1.toString() + digito2.toString());
        }
        return retorno;
    }

    /**
     *
     * @param string
     * @param preenche
     * @param tamanho
     * @return
     */
    public static String preencherDireita(String string, char preenche, int tamanho) {

        char[] array = new char[tamanho];
        int len = tamanho - string.length();
        for (int i = 0; i < len; i++) {
            array[i] = preenche;
        }
        string.getChars(0, string.length(), array, tamanho - string.length());
        return String.valueOf(array);
    }

     /**
     * Calcula a criptografia MD5 de uma String.
     *
     * @param campo String a ser calculada.
     * @return String md5.
     */
    public static String calcularMD5(String campo) {
        try {
            String s = campo;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            BigInteger md5Inteiro = new BigInteger(1, m.digest());

            String out = String.format("%032x", md5Inteiro);
            return out;
            //writer.println(out);
        } catch (Exception e) {
            return "";
        }

    }

     /**
     * Testa codigo GTIN-8, GTIN-12, GTIN-13, GTIN-14, GSIN, SSCC.
     *
     * @param codigoBarras String a ser testada.
     * @return true se codigo de barras for valido.
     */
    public static boolean isCodigoValido(String codigoBarras) {

        if (codigoBarras == null || codigoBarras.equals("")) {
            return false;
        }

        if (verificaPais(Integer.parseInt(codigoBarras.substring(0, 3))) == null) {
            return false;
        }

        int tamanho = codigoBarras.length();
        int multiplicador = 3;
        int total = 0;

        for (int i = tamanho - 2; i >= 0; i--) {
            total = total + (Integer.parseInt("" + codigoBarras.charAt(i)) * multiplicador);
            if (multiplicador == 3) {
                multiplicador = 1;
            } else {
                multiplicador = 3;
            }
        }

        int resto = total % 10;
        if (resto == 0) {
            return true;
        } else {
            int resultado = ((total + 10) - (resto)) - total;
            int verificador = Integer.parseInt("" + codigoBarras.charAt(codigoBarras.length() - 1));
            if (resultado == verificador) {
                return true;
            }
        }

        return false;
    }

    /**
     * Verifica qual pais pertence o codigo de barras.
     *
     * @param codigoBarras Integer a ser verificado.
     * @return null se codigo de barras for Invalido ou String com o nome do pais se o codigo for valido.
     */
    public static String verificaPais(int codigoBarras) {

        String retorno = null;
        if ((codigoBarras >= 789) && (codigoBarras <= 790)) {
            return "Brasil";
        };
        if ((codigoBarras >= 2) && (codigoBarras <= 19)) {
            return "E.U.A.";
        };
        if ((codigoBarras >= 20) && (codigoBarras <= 29)) {
            return "Distribuição restringida\n"
                    + "definido pela organização membro GS1";
        };
        if ((codigoBarras >= 30) && (codigoBarras <= 39)) {
            return "E.U.A (reservado para medicamentos)";
        };
        if ((codigoBarras >= 40) && (codigoBarras <= 49)) {
            return "Distribuição restringida\n"
                    + "definido pela organização membro GS1";
        };
        if ((codigoBarras >= 50) && (codigoBarras <= 59)) {
            return "Coupons";
        };
        if ((codigoBarras >= 60) && (codigoBarras <= 139)) {
            return "E.U.A.";
        };
        if ((codigoBarras >= 200) && (codigoBarras <= 299)) {
            return "Distribuição restringida\n"
                    + "definido pela organização membro GS1";
        };
        if ((codigoBarras >= 300) && (codigoBarras <= 379)) {
            return "França";
        };
        if ((codigoBarras >= 380) && (codigoBarras <= 380)) {
            return "Bulgária";
        };
        if ((codigoBarras >= 383) && (codigoBarras <= 383)) {
            return "Eslovénia";
        };
        if ((codigoBarras >= 385) && (codigoBarras <= 385)) {
            return "Croácia";
        };
        if ((codigoBarras >= 387) && (codigoBarras <= 387)) {
            return "Bósnia e Herzegovina";
        };
        if ((codigoBarras >= 400) && (codigoBarras <= 440)) {
            return "Alemanha";
        };
        if ((codigoBarras >= 450) && (codigoBarras <= 459)) {
            return "Japão";
        };
        if ((codigoBarras >= 490) && (codigoBarras <= 499)) {
            return "Japão";
        };
        if ((codigoBarras >= 460) && (codigoBarras <= 469)) {
            return "Rússia";
        };
        if ((codigoBarras >= 470) && (codigoBarras <= 470)) {
            return "Quirguistão";
        };
        if ((codigoBarras >= 471) && (codigoBarras <= 471)) {
            return "Ilha de Taiwan";
        };
        if ((codigoBarras >= 474) && (codigoBarras <= 474)) {
            return "Estônia";
        };
        if ((codigoBarras >= 475) && (codigoBarras <= 475)) {
            return "Letônia";
        };
        if ((codigoBarras >= 476) && (codigoBarras <= 476)) {
            return "Azerbaijão";
        };
        if ((codigoBarras >= 477) && (codigoBarras <= 477)) {
            return "Lituânia";
        };
        if ((codigoBarras >= 478) && (codigoBarras <= 478)) {
            return "Usbequistão";
        };
        if ((codigoBarras >= 479) && (codigoBarras <= 479)) {
            return "Sri Lanka";
        };
        if ((codigoBarras >= 480) && (codigoBarras <= 480)) {
            return "Filipinas";
        };
        if ((codigoBarras >= 481) && (codigoBarras <= 481)) {
            return "Bielorrússia";
        };
        if ((codigoBarras >= 482) && (codigoBarras <= 482)) {
            return "Ucrânia";
        };
        if ((codigoBarras >= 484) && (codigoBarras <= 484)) {
            return "Moldávia";
        };
        if ((codigoBarras >= 485) && (codigoBarras <= 485)) {
            return "Armênia";
        };
        if ((codigoBarras >= 486) && (codigoBarras <= 486)) {
            return "Geórgia";
        };
        if ((codigoBarras >= 487) && (codigoBarras <= 487)) {
            return "Cazaquistão";
        };
        if ((codigoBarras >= 489) && (codigoBarras <= 489)) {
            return "Hong Kong";
        };
        if ((codigoBarras >= 500) && (codigoBarras <= 509)) {
            return "Reino Unido";
        };
        if ((codigoBarras >= 520) && (codigoBarras <= 521)) {
            return "Grécia";
        };
        if ((codigoBarras >= 528) && (codigoBarras <= 528)) {
            return "Líbano";
        };
        if ((codigoBarras >= 529) && (codigoBarras <= 529)) {
            return "Chipre";
        };
        if ((codigoBarras >= 530) && (codigoBarras <= 530)) {
            return "Albânia";
        };
        if ((codigoBarras >= 531) && (codigoBarras <= 531)) {
            return "República da Macedônia";
        };
        if ((codigoBarras >= 535) && (codigoBarras <= 535)) {
            return "Malta";
        };
        if ((codigoBarras >= 539) && (codigoBarras <= 539)) {
            return "República da Irlanda";
        };
        if ((codigoBarras >= 540) && (codigoBarras <= 549)) {
            return "Bélgica & Luxemburgo";
        };
        if ((codigoBarras >= 560) && (codigoBarras <= 560)) {
            return "Portugal";
        };
        if ((codigoBarras >= 569) && (codigoBarras <= 569)) {
            return "Islândia";
        };
        if ((codigoBarras >= 570) && (codigoBarras <= 579)) {
            return "Dinamarca";
        };
        if ((codigoBarras >= 590) && (codigoBarras <= 590)) {
            return "Polónia";
        };
        if ((codigoBarras >= 594) && (codigoBarras <= 594)) {
            return "Romênia";
        };
        if ((codigoBarras >= 599) && (codigoBarras <= 599)) {
            return "Hungria";
        };
        if ((codigoBarras >= 600) && (codigoBarras <= 601)) {
            return "África do Sul";
        };
        if ((codigoBarras >= 603) && (codigoBarras <= 603)) {
            return "Gana";
        };
        if ((codigoBarras >= 608) && (codigoBarras <= 608)) {
            return "Bahrein";
        };
        if ((codigoBarras >= 609) && (codigoBarras <= 609)) {
            return "lhas Maurício";
        };
        if ((codigoBarras >= 611) && (codigoBarras <= 611)) {
            return "Marrocos";
        };
        if ((codigoBarras >= 613) && (codigoBarras <= 613)) {
            return "Argélia";
        };
        if ((codigoBarras >= 616) && (codigoBarras <= 616)) {
            return "Quênia";
        };
        if ((codigoBarras >= 618) && (codigoBarras <= 618)) {
            return "Costa do Marfim";
        };
        if ((codigoBarras >= 619) && (codigoBarras <= 619)) {
            return "Tunísia";
        };
        if ((codigoBarras >= 621) && (codigoBarras <= 621)) {
            return "Síria";
        };
        if ((codigoBarras >= 622) && (codigoBarras <= 622)) {
            return "Egito";
        };
        if ((codigoBarras >= 624) && (codigoBarras <= 624)) {
            return "Líbia";
        };
        if ((codigoBarras >= 625) && (codigoBarras <= 625)) {
            return "Jordânia";
        };
        if ((codigoBarras >= 626) && (codigoBarras <= 626)) {
            return "Irã";
        };
        if ((codigoBarras >= 627) && (codigoBarras <= 627)) {
            return "Kuwait";
        };
        if ((codigoBarras >= 628) && (codigoBarras <= 628)) {
            return "Arábia Saudita";
        };
        if ((codigoBarras >= 629) && (codigoBarras <= 629)) {
            return "Emirados Árabes Unidos";
        };
        if ((codigoBarras >= 640) && (codigoBarras <= 649)) {
            return "Finlândia";
        };
        if ((codigoBarras >= 690) && (codigoBarras <= 699)) {
            return "República Popular da China";
        };
        if ((codigoBarras >= 700) && (codigoBarras <= 709)) {
            return "Noruega";
        };
        if ((codigoBarras >= 729) && (codigoBarras <= 729)) {
            return "Israel";
        };
        if ((codigoBarras >= 730) && (codigoBarras <= 739)) {
            return "Suécia";
        };
        if ((codigoBarras >= 740) && (codigoBarras <= 740)) {
            return "Guatemala";
        };
        if ((codigoBarras >= 741) && (codigoBarras <= 741)) {
            return "El Salvador";
        };
        if ((codigoBarras >= 742) && (codigoBarras <= 742)) {
            return "Honduras";
        };
        if ((codigoBarras >= 743) && (codigoBarras <= 743)) {
            return "Nicarágua";
        };
        if ((codigoBarras >= 744) && (codigoBarras <= 744)) {
            return "Costa Rica";
        };
        if ((codigoBarras >= 745) && (codigoBarras <= 745)) {
            return "Panamá";
        };
        if ((codigoBarras >= 746) && (codigoBarras <= 746)) {
            return "República Dominicana";
        };
        if ((codigoBarras >= 750) && (codigoBarras <= 750)) {
            return "México";
        };
        if ((codigoBarras >= 754) && (codigoBarras <= 755)) {
            return "Canadá";
        };
        if ((codigoBarras >= 759) && (codigoBarras <= 759)) {
            return "Venezuela";
        };
        if ((codigoBarras >= 760) && (codigoBarras <= 769)) {
            return "Suíça";
        };
        if ((codigoBarras >= 770) && (codigoBarras <= 770)) {
            return "Colômbia";
        };
        if ((codigoBarras >= 773) && (codigoBarras <= 773)) {
            return "Uruguai";
        };
        if ((codigoBarras >= 775) && (codigoBarras <= 775)) {
            return "Peru";
        };
        if ((codigoBarras >= 777) && (codigoBarras <= 777)) {
            return "Bolívia";
        };
        if ((codigoBarras >= 779) && (codigoBarras <= 779)) {
            return "Argentina";
        };
        if ((codigoBarras >= 780) && (codigoBarras <= 780)) {
            return "Chile";
        };
        if ((codigoBarras >= 784) && (codigoBarras <= 784)) {
            return "Paraguai";
        };
        if ((codigoBarras >= 786) && (codigoBarras <= 786)) {
            return "Equador";
        };
        if ((codigoBarras >= 800) && (codigoBarras <= 839)) {
            return "Itália";
        };
        if ((codigoBarras >= 840) && (codigoBarras <= 849)) {
            return "Espanha";
        };
        if ((codigoBarras >= 850) && (codigoBarras <= 850)) {
            return "Cuba";
        };
        if ((codigoBarras >= 858) && (codigoBarras <= 858)) {
            return "Eslováquia";
        };
        if ((codigoBarras >= 859) && (codigoBarras <= 859)) {
            return "República Checa";
        };
        if ((codigoBarras >= 860) && (codigoBarras <= 860)) {
            return "Sérvia e Montenegro";
        };
        if ((codigoBarras >= 865) && (codigoBarras <= 865)) {
            return "Mongólia";
        };
        if ((codigoBarras >= 867) && (codigoBarras <= 867)) {
            return "Coreia do Norte";
        };
        if ((codigoBarras >= 868) && (codigoBarras <= 869)) {
            return "Turquia";
        };
        if ((codigoBarras >= 870) && (codigoBarras <= 879)) {
            return "Holanda";
        };
        if ((codigoBarras >= 880) && (codigoBarras <= 880)) {
            return "Coreia do Sul";
        };
        if ((codigoBarras >= 884) && (codigoBarras <= 884)) {
            return "Cambodja";
        };
        if ((codigoBarras >= 885) && (codigoBarras <= 885)) {
            return "Tailândia";
        };
        if ((codigoBarras >= 888) && (codigoBarras <= 888)) {
            return "Singapura";
        };
        if ((codigoBarras >= 890) && (codigoBarras <= 890)) {
            return "Índia";
        };
        if ((codigoBarras >= 893) && (codigoBarras <= 893)) {
            return "Vietnam";
        };
        if ((codigoBarras >= 899) && (codigoBarras <= 899)) {
            return "Indonésia";
        };
        if ((codigoBarras >= 900) && (codigoBarras <= 919)) {
            return "Áustria";
        };
        if ((codigoBarras >= 930) && (codigoBarras <= 939)) {
            return "Austrália";
        };
        if ((codigoBarras >= 940) && (codigoBarras <= 949)) {
            return "Nova Zelândia";
        };
        if ((codigoBarras >= 950) && (codigoBarras <= 950)) {
            return "GS1 Global Office";
        };
        if ((codigoBarras >= 955) && (codigoBarras <= 955)) {
            return "Malásia";
        };
        if ((codigoBarras >= 958) && (codigoBarras <= 958)) {
            return "Macau";
        };
        if ((codigoBarras >= 977) && (codigoBarras <= 977)) {
            return "Publicações periódicas seriadas (ISSN)";
        };
        if ((codigoBarras >= 978) && (codigoBarras <= 979)) {
            return "International ISBN Agency";
        };
        if ((codigoBarras >= 980) && (codigoBarras <= 980)) {
            return "Refund receipts";
        };
        if ((codigoBarras >= 981) && (codigoBarras <= 982)) {
            return "Coupons e meios de pagamento";
        };
        if ((codigoBarras >= 990) && (codigoBarras <= 999)) {
            return "Coupons";
        };

        return retorno;
    }

}
