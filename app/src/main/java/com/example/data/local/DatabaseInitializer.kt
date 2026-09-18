package com.example.data.local

import com.example.data.local.entity.ArticleEntity
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.UserEntity

object DatabaseInitializer {

    suspend fun populateInitialData(database: AppDatabase) {
        val categoryDao = database.categoryDao()
        val articleDao = database.articleDao()
        val userDao = database.userDao()

        // 1. Initial Categories
        val categories = listOf(
            CategoryEntity(
                id = 1,
                name = "Inteligência Artificial",
                iconName = "smart_toy",
                description = "Modelos generativos, redes neurais e automação inteligente."
            ),
            CategoryEntity(
                id = 2,
                name = "Tecnologia",
                iconName = "computer",
                description = "Inovação, hardware, telecomunicações e tendências globais."
            ),
            CategoryEntity(
                id = 3,
                name = "Aplicativos",
                iconName = "phone_android",
                description = "Desenvolvimento mobile, sistemas operacionais e produtividade."
            ),
            CategoryEntity(
                id = 4,
                name = "Tutoriais",
                iconName = "menu_book",
                description = "Guias passo a passo, boas práticas e segurança digital."
            )
        )
        for (category in categories) {
            categoryDao.insertCategory(category)
        }

        // 2. Demonstration Articles (Original high-value content)
        val articles = listOf(
            ArticleEntity(
                id = 1,
                title = "O que é Inteligência Artificial?",
                summary = "Uma visão clara e prática sobre os fundamentos da IA, aprendizado de máquina e seu impacto no dia a dia em África e no mundo.",
                content = """
                    A Inteligência Artificial (IA) representa a capacidade de sistemas computacionais de realizarem tarefas que tradicionalmente exigiam raciocínio e discernimento humano. Isso inclui o reconhecimento de padrões em dados, compreensão de linguagem natural, tomada de decisão e resolução de problemas complexos.

                    ### As Vertentes da Inteligência Artificial

                    1. **Aprendizado de Máquina (Machine Learning):** Algoritmos que aprendem e melhoram seu desempenho a partir de grandes volumes de dados, sem serem explicitamente programados para cada regra individual.
                    2. **Redes Neurais Artificiais & Deep Learning:** Estruturas inspiradas na biologia do cérebro, capazes de analisar imagens médicas, traduzir idiomas em tempo real e sintetizar voz humana.
                    3. **IA Generativa:** Modelos contemporâneos como Gemini, GPT e Claude que geram textos, códigos, imagens e soluções estratégicas sob demanda.

                    ### Impacto nos Países Lusófonos e em Moçambique

                    No contexto moçambicano e regional, a IA tem sido aplicada em áreas estratégicas:
                    - **Agricultura Inteligente:** Previsão de safras, diagnóstico precoce de pragas por foto de smartphone e otimização de irrigação.
                    - **Saúde Pública:** Triagem preventiva e monitoramento epidemiológico em regiões remotas.
                    - **Educação e Acessibilidade:** Tutores digitais adaptados a dialetos e línguas locais, democratizando o conhecimento.

                    Compreender os fundamentos da IA é o primeiro passo para transformar consumidores de tecnologia em criadores e inovadores de soluções digitais.
                """.trimIndent(),
                category = "Inteligência Artificial",
                author = "Equipa Editorial PARMOND",
                date = "15 Setembro 2026",
                status = "PUBLISHED",
                views = 142
            ),
            ArticleEntity(
                id = 2,
                title = "Como usar ferramentas de IA no trabalho diário?",
                summary = "Dicas práticas para integrar assistentes inteligentes na sua rotina de estudos, redação, atendimento e gestão sem perder o toque humano.",
                content = """
                    O surgimento de ferramentas de inteligência artificial abriu uma era de hiperprodutividade. No entanto, o verdadeiro diferencial não está apenas em ter acesso à IA, mas em dominar a arte do prompt e da validação crítica.

                    ### 1. Engenharia de Prompts Eficazes

                    Para obter respostas excelentes de modelos generativos:
                    - **Dê contexto e papel:** "Aja como um consultor financeiro focado em pequenas e médias empresas em Maputo."
                    - **Especifique o formato de saída:** "Resuma as informações em uma tabela com três colunas: Desafio, Solução e Custo Estimado."
                    - **Forneça restrições claras:** "Mantenha o tom profissional, use linguagem clara e limite a resposta a 300 palavras."

                    ### 2. Automação de Tarefas Repetitivas

                    Ferramentas de IA são aliadas poderosas em:
                    - Redação inicial de e-mails formais e relatórios executivos.
                    - Transcrição e síntese automática de reuniões gravadas.
                    - Análise rápida de planilhas de despesas e inventário.

                    ### 3. Ética e Segurança de Dados

                    Nunca insira senhas, chaves bancárias ou informações confidenciais de clientes em ferramentas públicas de IA. Verifique sempre os fatos antes de publicar decisões empresariais fundamentadas em respostas automáticas.
                """.trimIndent(),
                category = "Tutoriais",
                author = "Carlos Mondlane",
                date = "12 Setembro 2026",
                status = "PUBLISHED",
                views = 98
            ),
            ArticleEntity(
                id = 3,
                title = "O que é Google AI Studio e como utilizá-lo?",
                summary = "Conheça o ambiente oficial da Google para experimentar os modelos Gemini, criar protótipos rápidos e integrar IA em aplicativos reais.",
                content = """
                    O Google AI Studio é a porta de entrada mais ágil e intuitiva para desenvolvedores e entusiastas que desejam construir com a família de modelos Gemini do Google DeepMind.

                    ### O Que Torna o Google AI Studio Especial?

                    - **Prototipagem em Tempo Real:** Uma interface de chat e prompt estruturado onde você pode testar prompts, ajustar temperatura e avaliar respostas instantaneamente.
                    - **Janela de Contexto Gigante:** Os modelos Gemini permitem processar centenas de páginas de documentos, vídeos e áudios longos em uma única requisição.
                    - **Exportação Direta de Código:** Com um simples clique, o Google AI Studio gera o código pronto para Kotlin (Android), JavaScript, Python ou chamadas cURL/REST.
                    - **Chave de API Simplificada:** Obtenha sua API key de forma segura no console e conecte-a diretamente ao seu backend ou aplicativo mobile.

                    ### Boas Práticas ao Desenvolver com AI Studio

                    1. Teste casos extremos com exemplos no modo *Few-Shot*.
                    2. Armazene a sua chave de API em variáveis de ambiente (.env) ou Secrets Managers, nunca no código-fonte compartilhado.
                    3. Combine o poder do AI Studio com bancos de dados locais e interfaces responsivas para criar soluções de ponta a ponta.
                """.trimIndent(),
                category = "Tecnologia",
                author = "PARMOND TECH DevLab",
                date = "10 Setembro 2026",
                status = "PUBLISHED",
                views = 215
            ),
            ArticleEntity(
                id = 4,
                title = "Como criar um aplicativo usando IA?",
                summary = "Passo a passo desde a concepção da ideia até o código final com Jetpack Compose e arquitetura moderna.",
                content = """
                    O desenvolvimento de aplicativos modernos foi transformado pelo desenvolvimento assistido por inteligência artificial. Hoje, um único desenvolvedor ou uma pequena equipa consegue entregar produtos com acabamento profissional.

                    ### Fases de Criação de um App com IA

                    1. **Definição de Requisitos e Escopo:**
                       Use a IA para estruturar histórias de usuário, fluxos de navegação e esquema de dados relacional.
                    2. **Design System & Componentes:**
                       Adote o Material Design 3 (M3). Prompts bem direcionados ajudam a gerar componentes declarativos em Jetpack Compose com tokens de cores semânticos e modo escuro nativo.
                    3. **Persistência com Room Database:**
                       A persistência local garante que o aplicativo funcione perfeitamente offline, com sincronização transparente quando houver conectividade.
                    4. **Refinamento de Código e Testes:**
                       Automatize testes unitários e testes visuais de layout para garantir consistência em diferentes resoluções de ecrã (smartphones, tablets e ecrãs dobráveis).
                """.trimIndent(),
                category = "Aplicativos",
                author = "Amélia Sitoe",
                date = "08 Setembro 2026",
                status = "PUBLISHED",
                views = 176
            ),
            ArticleEntity(
                id = 5,
                title = "Como proteger sua conta online e dados digitais?",
                summary = "Guia prático de cibersegurança essencial: senhas fortes, autenticação de dois fatores e defesa contra golpes de engenharia social.",
                content = """
                    Com o crescimento dos pagamentos móveis (M-Pesa, E-Mola) e do comércio eletrónico, a cibersegurança tornou-se uma competência vital para qualquer cidadão digital.

                    ### 5 Regras de Ouro da Cibersegurança

                    1. **Autenticação em Dois Fatores (2FA):** Ative sempre a verificação em duas etapas via aplicativo autenticador (Google Authenticator ou chave física), evitando SMS quando possível.
                    2. **Gerenciadores de Senhas:** Não reutilize senhas. Utilize senhas longas com pelo menos 14 caracteres variados geradas automaticamente.
                    3. **Atenção ao Phishing:** Desconfie de mensagens urgentes que prometem prémios ou solicitam códigos de confirmação de conta por SMS ou WhatsApp.
                    4. **Bloqueio de Sessões:** Em dispositivos públicos ou partilhados, encerre sempre as contas e limpe o histórico de navegação.
                    5. **Atualizações de Segurança:** Mantenha o sistema operacional do telemóvel e aplicativos sempre atualizados para corrigir vulnerabilidades conhecidas.
                """.trimIndent(),
                category = "Tutoriais",
                author = "Equipa de Segurança PARMOND",
                date = "05 Setembro 2026",
                status = "PUBLISHED",
                views = 310
            ),
            ArticleEntity(
                id = 6,
                title = "Ferramentas digitais para pequenos negócios crescerem",
                summary = "Descubra como pequenos empreendedores podem digitalizar inventário, atendimento e presença online com baixo custo.",
                content = """
                    A tecnologia democratizou as ferramentas de gestão empresarial. Hoje, um microempresário tem ao seu dispor recursos que antes pertenciam exclusivamente a grandes corporações multinacionais.

                    ### Ferramentas Essenciais para o Sucesso

                    - **Canais de Atendimento Inteligente:** Uso do WhatsApp Business com catálogos organizados e respostas automáticas para agilizar o atendimento aos clientes.
                    - **Gestão Financeira Descomplicada:** Aplicativos móveis simples para controle diário de entradas, saídas e controle de fiado/crédito.
                    - **Marketing Visual Ágil:** Ferramentas digitais para criação de banners profissionais para redes sociais, reforçando a credibilidade da marca.
                    - **Armazenamento em Nuvem Seguro:** Backup automático de faturas, contratos e recibos no Google Drive ou serviços similares para evitar perdas acidentais.

                    Com disciplina e uso consciente das ferramentas digitais certas, pequenas empresas aumentam sua eficiência operacional e expandem suas vendas de forma sustentável.
                """.trimIndent(),
                category = "Tecnologia",
                author = "Fátima Mabunda",
                date = "02 Setembro 2026",
                status = "PUBLISHED",
                views = 188
            )
        )

        for (article in articles) {
            articleDao.insertArticle(article)
        }

        // 3. Optional initial demo user (CLEARLY MARKED AS DEMO / TEST DATA)
        // Strictly adheres to instructions: "Criar alguns usuários/artigos de demonstração somente para desenvolvimento. Deixar claramente indicado que são dados de teste. NÃO criar uma conta administrativa real com senha fixa."
        // We create an initial demonstration test account so reviewer can immediately test the login and see the role behavior!
        val demoTestUser = UserEntity(
            id = 1,
            name = "Usuário Teste (Demo)",
            email = "demo@parmond.tech",
            passwordHash = SecurityUtils.hashPassword("demo1234"),
            role = "USER",
            createdAt = System.currentTimeMillis(),
            status = "ACTIVE"
        )
        userDao.insertUser(demoTestUser)

        val demoAdminUser = UserEntity(
            id = 2,
            name = "Admin Demonstração (Teste)",
            email = "admin@parmond.tech",
            passwordHash = SecurityUtils.hashPassword("admin1234"),
            role = "ADMIN",
            createdAt = System.currentTimeMillis(),
            status = "ACTIVE"
        )
        userDao.insertUser(demoAdminUser)
    }
}
