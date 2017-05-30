/** 
 * @project Testa_assembler
 *
 * @brief Arquivo unico do projeto
 * @file Main.c
 * @author Cleber Jorge Amaral
 * @date Oct/2015
 *
 * Neste programa realizamos testes com diferentes estruturas lógicas para verificar
 * como o compilador monta a lógica (assembly). São feitos testes de tempo que servem
 * para dar uma melhor dimensão da eficiência do código. Mas podemos verificar também
 * o assembly gerado.
 * Os testes foram validados em simulação no proteus, atraves do osciloscopio virtual
 */
 
#include <reg52.h> 

sfr Porta_0 = 0x80;
sfr Porta_1 = 0x90;

void main() 
{
	unsigned char x = 0; 
	unsigned int i = 0;
	unsigned char c = 0;
	
	while (1)
	{

		/** Teste A
		 * Comparação de tempo que leva  um laço utilizando variável inteira
		 * Na estrutura 1 será incrementado o valor de 0 até 255, isso fará com que o compilador
		 * utilize estrutura de comparação de bytes (comparar se chegou a 0x00FF através de CJNE)
		 * Na estrutura 2 será incrementado de 65280 até 65536 (255 incrementos), com CJNEs, desta
		 * o compilador realiza comparações dos dois registradores de 8bits de uma forma mais eficiente.
		 * Resultados (clock da CPU 11.0592MHz simulado pelo keil e validados no osciloscopio do proteus):
		 * Estrutura 1 consome 2.216797ms
		 * Estrutura 2 consome 1.939019ms
		 */
		
		/* Estrutura 1 */
		for (i = 0;i<0x00FF;i++); 

		/* Utilizado no proteus para medir o tempo */
		Porta_1 = ~Porta_1; 
		
		/* Estrutura 2 */
		for (i = 0xFFFF-0x00FF;i<0xFFFF;i++);

		/* Utilizado no proteus para medir o tempo */
		Porta_1 = ~Porta_1;

		/** Teste B (aqui podemos também comparar com os resultados do teste A já que o range é 255 também)
		 * Comparação de tempo que leva um laço utilizando variável char 
		 * Na estrutura 3 será incrementado o valor de 0 até 255, comparação com CJNE
		 * Na estrutura 4 será decrementado de 255 até 0, utilizando DJNZ que é bem mais eficiente (decrementa e compara)
		 * Outro resultado que podemos obter seria comparando as estruturas 1 e 2
		 * com estas realizadas no teste B, que mostra a otimização quando se trata
		 * de variáveis int e char (para range que seriam suportados por uma char)
		 * Resultados (clock da CPU 11.0592MHz simulado pelo keil e validados no osciloscopio do proteus):
		 * Estrutura 3 consome 0,832248ms
		 * Estrutura 4 consome 0,55447ms
		 */

		/* Estrutura 3 */
	  for (c = 0;c<0xFF;c++);
		
		/* Utilizado no proteus para medir o tempo */
		Porta_1 = ~Porta_1;

		/* Estrutura 4 */
		for (c = 0xFF;c > 0;c--);

		/* Utilizado no proteus para medir o tempo */
		Porta_1 = ~Porta_1;


		/** Teste C
		 * Comparação de tempo que leva um laço utilizando variável char 
		 * Na estrutura 5 será incrementado o valor de 0 até 128 (meio de escala), utilizando CNJE.
		 * Na estrutura 6 será decrementado de 128 até 0 (inicio de escala), utilizando DJNZ.
		 * Resultados (clock da CPU 11.0592MHz simulado pelo keil e validados no osciloscopio do proteus):
		 * Estrutura 5 consome 0,418836ms
		 * Estrutura 6 consome 0,278862ms
		 */

		/* Estrutura 5 */
		for (c = 0;c < 0x80;c++);

		/* Utilizado no proteus para medir o tempo */
		Porta_1 = ~Porta_1;

		/* Estrutura 6 */
		for (c = 0x80;c > 0;c--);

		/* Utilizado no proteus para medir o tempo */
		Porta_1 = ~Porta_1;


		Porta_0 = x++;
	}
}