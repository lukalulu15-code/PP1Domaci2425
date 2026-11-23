# MikroJava kompajler

Kompajler za jezik **MikroJava**, urađen kao projekat iz predmeta *Programski prevodioci 1* (ETF Beograd).
Prevodi izvorni `.mj` program u bajtkod (`.obj`) koji se izvršava na MikroJava virtuelnoj mašini.

Kompajler prolazi kroz četiri faze:

1. **Leksička analiza** – JFlex (`spec/mjlexer.flex`)
2. **Sintaksna analiza** – CUP sa automatskom izgradnjom AST stabla (`spec/mjparser.cup`)
3. **Semantička analiza** – obilazak AST-a i rad sa tabelom simbola (`SemanticAnalyzer.java`)
4. **Generisanje koda** – MikroJava bajtkod (`CodeGenerator.java`)

## Podržane jezičke konstrukcije

- Deklaracije konstanti (`int`, `char`, `bool`) i promenljivih, uključujući nizove
- Globalne i lokalne promenljive, metoda `void main()`
- Aritmetički izrazi: `+`, `-`, `*`, `/`, `%`, unarni minus, zagrade
- Dodela, inkrement `++` i dekrement `--`
- `read(...)` i `print(...)` (uz opcionu širinu ispisa: `print(x, 5)`)
- Alokacija nizova: `new int[n]`, `new char[n]`, ...
- Ugrađene funkcije: `ord`, `chr`, `len`
- Komentari u jednoj liniji (`//`)
- Oporavak od sintaksnih grešaka u deklaracijama i iskazima dodele

### Tip `set` (skup celih brojeva)

Kao proširenje jezika, podržan je tip `set`:

| Konstrukcija            | Značenje                                         |
|-------------------------|--------------------------------------------------|
| `s = new set[n];`       | pravi skup kapaciteta `n`                        |
| `add(s, x);`            | dodaje element `x` u skup (bez duplikata)        |
| `addAll(s, niz);`       | dodaje sve elemente `int` niza u skup            |
| `s3 = s1 union s2;`     | unija dva skupa                                  |
| `print(s);`             | ispisuje elemente skupa                          |

## Primer programa

```java
program primer

const int pi = 98;
int niz[];
set s1, s2;

{
    void main()
        set s3;
    {
        niz = new int[3];
        niz[0] = 1; niz[1] = 2; niz[2] = 2;

        s1 = new set[4];
        s2 = new set[4];
        s3 = new set[8];

        addAll(s1, niz);     // s1 = {1, 2}
        add(s2, pi);
        add(s2, 7);          // s2 = {98, 7}

        s3 = s1 union s2;
        print(s3);
    }
}
```

Više primera se nalazi u folderu [`test/`](test/) (`program.mj`, `test301.mj`, `test302.mj`, `test303.mj`).

## Struktura projekta

```
.
├── build.xml          # Ant skripta (generisanje leksera/parsera, kompajliranje, pokretanje)
├── config/log4j.xml   # podešavanja logovanja
├── lib/               # JFlex, CUP, log4j, symboltable i mj-runtime biblioteke
├── spec/              # specifikacija leksera (.flex) i gramatike (.cup)
├── src/rs/ac/bg/etf/pp1/
│   ├── Compiler.java          # ulazna tačka – pokreće sve faze kompajliranja
│   ├── SemanticAnalyzer.java  # semantička analiza
│   ├── CodeGenerator.java     # generisanje bajtkoda
│   ├── ast/                   # AST klase (generiše CUP)
│   ├── MJParser.java, sym.java, Yylex.java   # generisani parser i lekser
│   └── util/Log4JUtils.java
├── test/              # test programi (.mj), ulaz za read (inputs.txt) i izlazi
└── logs/              # log fajlovi kompajlera (nastaju pri pokretanju)
```

## Pokretanje

### Preduslovi

- Java (JDK 8 ili noviji)
- Apache Ant (ili Eclipse sa ugrađenim Ant-om)

### 1. Generisanje leksera i parsera i kompajliranje

```bash
ant compile
```

Ovaj target redom briše stare generisane fajlove, pokreće JFlex i CUP, ispravlja paket AST klasa i kompajlira izvorni kod.

### 2. Kompajliranje MikroJava programa

Pokrenuti klasu `rs.ac.bg.etf.pp1.Compiler` (npr. iz Eclipse-a). Kompajler čita `test/test301.mj` i, ukoliko nema grešaka,
upisuje bajtkod u `test/test301.obj`. Sintaksno stablo, tabela simbola i greške se ispisuju u log (`logs/mj.log`).

### 3. Izvršavanje generisanog koda

```bash
ant runObj
```

Ovaj target najpre disasemblira `.obj` fajl (`disasm`), zatim ga pokreće u debug režimu (`debugObj`) i na kraju normalno izvršava (`runObj`).
Ulaz za `read` naredbe se čita iz `test/inputs.txt`.

## Korišćene biblioteke

- [JFlex](https://jflex.de/) – generator leksičkog analizatora
- CUP (ETF verzija sa podrškom za AST) – generator parsera
- log4j 1.2.17 – logovanje
- `symboltable-1-1.jar` – tabela simbola
- `mj-runtime-1.1.jar` – MikroJava VM, disasembler i `Code` klasa za generisanje koda
