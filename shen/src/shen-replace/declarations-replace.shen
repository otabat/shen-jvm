\*

Copyright (c) 2010-2015, Mark Tarver

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. The name of Mark Tarver may not be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Mark Tarver ''AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Mark Tarver BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*\

(package shen []

(set *installing-kl* false)
(set *history* [])
(set *tc* false)
(set *property-vector* (dict 20000))
(set *process-counter* 0)
(set *varcounter* (vector 1000))
(set *prologvectors* (vector 1000))
(set *demodulation-function* (/. X X))
(set *macroreg* [timer-macro cases-macro abs-macro put/get-macro
                 compile-macro datatype-macro let-macro assoc-macro
                 make-string-macro output-macro input-macro error-macro
                 prolog-macro synonyms-macro nl-macro
                 @s-macro defprolog-macro function-macro])
(set *macros*
     [(/. X (timer-macro X))
      (/. X (cases-macro X))
      (/. X (abs-macro X))
      (/. X (put/get-macro X))
      (/. X (compile-macro X))
      (/. X (datatype-macro X))
      (/. X (let-macro X))
      (/. X (assoc-macro X))
      (/. X (make-string-macro X))
      (/. X (output-macro X))
      (/. X (input-macro X))
      (/. X (error-macro X))
      (/. X (prolog-macro X))
      (/. X (synonyms-macro X))
      (/. X (nl-macro X))
      (/. X (@s-macro X))
      (/. X (defprolog-macro X))
      (/. X (function-macro X))])
(set *gensym* 0)
(set *tracking* [])
(set *alphabet* [A B C D E F G H I J K L M N O P Q R S T U V W X Y Z])
(set *special* [@p @s @v cons lambda let where set open])
(set *extraspecial* [define process-datatype input+ defcc read+ defmacro])
(set *spy* false)
(set *datatypes* [])
(set *alldatatypes* [])
(set *shen-type-theory-enabled?* true)
(set *synonyms* [])
(set *system* [])
(set *signedfuncs* [])
(set *maxcomplexity* 128)
(set *occurs* true)
(set *maxinferences* 1000000)
(set *maximum-print-sequence-size* 20)
(set *catch* 0)
(set *call* 0)
(set *infs* 0)
(set *hush* false)
(set *optimise* false)
(set *version* "Shen 20.1")

(if (not (bound? *home-directory*))
    (set *home-directory* "")
    skip)

(if (not (bound? *sterror*))
    (set *sterror* (value *stoutput*))
    skip)

(if (not (bound? *argv*))
    (set *argv* ["shen"])
    skip)

(define initialise_arity_table
  [] -> []
  [F Arity | Table] -> (let DecArity (put F arity Arity)
                         (initialise_arity_table Table)))

(define arity
  F -> (get/or F arity (freeze -1)))

(initialise_arity_table
 [abort 0 absvector? 1 absvector 1 adjoin 2 and 2 append 2 arity 1
  assoc 2 boolean? 1 bound? 1 cd 1 close 1 compile 3 concat 2 cons 2 cons? 1
  command-line 0 cn 2 declare 2 destroy 1 difference 2 do 2 element? 2 empty? 1
  enable-type-theory 1 error-to-string 1 interror 2 eval 1
  eval-kl 1 exit 1 explode 1 external 1 fail-if 2 fail 0 fix 2
  fold-left 3 fold-right 3 filter 2
  for-each 2 findall 5 freeze 1 fst 1 gensym 1 get 3 get/or 4
  get-time 1 address-> 3 <-address 2 <-address/or 3 <-vector 2 <-vector/or 3
  > 2 >= 2 = 2
  hash 2 hd 1 hdv 1 hdstr 1 head 1 if 3 integer? 1
  intern 1 identical 4 inferences 0 input 1 input+ 2 implementation 0
  intersection 2 internal 1 it 0 kill 0 language 0
  length 1 limit 1 lineread 1 load 1 < 2 <= 2 vector 1 macroexpand 1 map 2
  mapcan 2 maxinferences 1 nl 1 not 1 nth 2
  n->string 1 number? 1 occurs-check 1 occurrences 2 occurs-check 1
  open 2 optimise 1 or 2 os 0 package 3 package? 1
  port 0 porters 0 pos 2 print 1 profile 1 profile-results 1 pr 2
  ps 1 preclude 1 preclude-all-but 1 protect 1
  address-> 3 put 4 reassemble 2 read-file-as-string 1 read-file 1
  read-file-as-charlist 1 read-file-as-bytelist 1
  read 1 read-byte 1 read-from-string 1 read-char-code 1
  receive 1 release 0 remove 2 require 3 reverse 1 set 2
  simple-error 1 snd 1 specialise 1 spy 1 step 1 stinput 0 stoutput 0 sterror 0
  string->n 1 string->symbol 1 string? 1 str 1 subst 3 sum 1
  symbol? 1 systemf 1 tail 1 tl 1 tc 1 tc? 0
  thaw 1 tlstr 1 track 1 trap-error 2 tuple? 1 type 2
  return 3 undefmacro 1 unput 3 unprofile 1 unify 4 unify! 4
  union 2 untrack 1 unspecialise 1 undefmacro 1
  vector 1 vector? 1 vector-> 3 value 1 value/or 2 variable? 1 version 0
  write-byte 2 write-to-file 2 y-or-n? 1 + 2 * 2 / 2 - 2 == 2
  <e> 1 <!> 1 @p 2 @v 2 @s 2 preclude 1 include 1
  preclude-all-but 1 include-all-but 1
  dict 1 dict? 1 dict-count 1 dict-> 3 <-dict/or 3 <-dict 2 dict-rm 2
  dict-fold 3 dict-keys 1 dict-values 1
  ])

(define systemf
  F -> (let Shen (intern "shen")
            External (get Shen external-symbols)
            Place (put Shen external-symbols (adjoin F External))
          F))

(define adjoin
  X Y -> (if (element? X Y) Y [X | Y]))

(put (intern "shen") external-symbols
     [! } { --> <-- && : ; :- := _
      *language* *implementation* *stinput* *stoutput* *sterror*
      *home-directory* *version* *argv*
      *maximum-print-sequence-size* *macros* *os* *release* *property-vector*
      *port* *porters* *hush*
      @v @p @s
      <- -> <e> <!> == = >= > /. =! $ - / * + <= < >> <>
      y-or-n? write-to-file write-byte where when warn version verified
      variable? value value/or vector-> <-vector <-vector/or vector vector?
      unspecialise untrack unit unix union unify
      unify! unput unprofile undefmacro return type tuple? true
      trap-error track time thaw tc? tc tl tlstr tlv
      tail systemf synonyms symbol symbol? string->symbol sum subst
      string? string->n stream string stinput sterror
      stoutput step spy specialise snd simple-error set save str run
      reverse remove release read receive
      read-file read-file-as-bytelist read-file-as-string read-byte
      read-file-as-charlist
      read-char-code read-from-string package? put preclude
      preclude-all-but ps prolog? protect profile-results profile print
      pr pos porters port package output out os or
      optimise open occurrences occurs-check n->string number? number
      null nth not nl mode macroexpand
      maxinferences mapcan map make-string load loaded list lineread
      limit length let lazy lambda language kill is
      intersection inferences intern integer? input input+ include
      include-all-but it in internal implementation if identical head
      hd hdv hdstr hash get get/or get-time gensym function fst freeze fix
      file fail fail-if fwhen findall for-each fold-right fold-left filter
      false enable-type-theory explode external exception eval-kl eval
      error-to-string error empty? exit
      element? do difference destroy defun define defmacro defcc
      defprolog declare datatype cut cn
      cons? cons cond concat compile cd cases call close bind bound?
      boolean? boolean bar! assoc arity abort
      append and adjoin <-address <-address/or address-> absvector? absvector
      dict dict? dict-count dict-> <-dict/or <-dict dict-rm dict-fold
      dict-keys dict-values
      command-line
      ])

(define lambda-form-entry
  \* package and receive are not real functions, but have arity *\
  package -> []
  receive -> []
  F -> (let ArityF (arity F)
         (cases (= ArityF -1) []
                (= ArityF 0) [] \\ change to [[F | F]] for CL if wanted
                true [[F | (eval-kl (lambda-form F ArityF))]])))

(define lambda-form
  F 0 -> F
  F N -> (let X (gensym (protect V))
           [lambda X (lambda-form (add-end F X) (- N 1))]))

(define add-end
  [F | Y] X -> (append [F | Y] [X])
  F X -> [F X])

(define set-lambda-form-entry
  [F | LambdaForm] -> (put F lambda-form LambdaForm))

(for-each
 (/. Entry (set-lambda-form-entry Entry))
 [[datatype-error | (/. X (datatype-error X))]
  [tuple | (/. X (tuple X))]
  [pvar | (/. X (pvar X))]
  [dictionary | (/. X (dictionary X))]
  | [[@v | (lambda V1 (lambda V2 (@v V1 V2)))]
     [@p | (lambda V3 (lambda V4 (@p V3 V4)))]
     [@s | (lambda V5 (lambda V6 (@s V5 V6)))]
     [<e> | (lambda V7 (<e> V7))]
     [<!> | (lambda V8 (<!> V8))]
     [== | (lambda V9 (lambda V10 (== V9 V10)))]
     [= | (lambda V11 (lambda V12 (= V11 V12)))]
     [>= | (lambda V13 (lambda V14 (>= V13 V14)))]
     [> | (lambda V15 (lambda V16 (> V15 V16)))]
     [- | (lambda V17 (lambda V18 (- V17 V18)))]
     [/ | (lambda V19 (lambda V20 (/ V19 V20)))]
     [* | (lambda V21 (lambda V22 (* V21 V22)))]
     [+ | (lambda V23 (lambda V24 (+ V23 V24)))]
     [<= | (lambda V25 (lambda V26 (<= V25 V26)))]
     [< | (lambda V27 (lambda V28 (< V27 V28)))]
     [y-or-n? | (lambda V29 (y-or-n? V29))]
     [write-to-file | (lambda V30 (lambda V31 (write-to-file V30 V31)))]
     [write-byte | (lambda V32 (lambda V33 (write-byte V32 V33)))]
     [variable? | (lambda V34 (variable? V34))]
     [value | (lambda V35 (value V35))]
     [value/or | (lambda V36 (lambda V37 (value/or V36 V37)))]
     [vector-> | (lambda V38 (lambda V39 (lambda V40 (vector-> V38 V39 V40))))]
     [<-vector | (lambda V41 (lambda V42 (<-vector V41 V42)))]
     [<-vector/or | (lambda V43 (lambda V44 (lambda V45 (<-vector/or V43 V44 V45))))]
     [vector | (lambda V46 (vector V46))]
     [vector? | (lambda V47 (vector? V47))]
     [unspecialise | (lambda V48 (unspecialise V48))]
     [untrack | (lambda V49 (untrack V49))]
     [union | (lambda V50 (lambda V51 (union V50 V51)))]
     [unify | (lambda V52 (lambda V53 (lambda V54 (lambda V55 (unify V52 V53 V54 V55)))))]
     [unify! | (lambda V56 (lambda V57 (lambda V58 (lambda V59 (unify! V56 V57 V58 V59)))))]
     [unput | (lambda V60 (lambda V61 (lambda V62 (unput V60 V61 V62))))]
     [unprofile | (lambda V63 (unprofile V63))]
     [undefmacro | (lambda V64 (undefmacro V64))]
     [return | (lambda V65 (lambda V66 (lambda V67 (return V65 V66 V67))))]
     [type | (lambda V68 (lambda V69 (type V68 V69)))]
     [tuple? | (lambda V70 (tuple? V70))]
     [trap-error | (lambda V71 (lambda V72 (trap-error V71 V72)))]
     [track | (lambda V73 (track V73))]
     [thaw | (lambda V74 (thaw V74))]
     [tc | (lambda V75 (tc V75))]
     [tl | (lambda V76 (tl V76))]
     [tlstr | (lambda V77 (tlstr V77))]
     [tail | (lambda V78 (tail V78))]
     [systemf | (lambda V79 (systemf V79))]
     [symbol? | (lambda V80 (symbol? V80))]
     [string->symbol | (lambda V81 (string->symbol V81))]
     [sum | (lambda V82 (sum V82))]
     [subst | (lambda V83 (lambda V84 (lambda V85 (subst V83 V84 V85))))]
     [string?|  (lambda V86 (string? V86))]
     [string->n | (lambda V87 (string->n V87))]
     [step | (lambda V88 (step V88))]
     [spy | (lambda V89 (spy V89))]
     [specialise | (lambda V90 (specialise V90))]
     [snd | (lambda V91 (snd V91))]
     [simple-error | (lambda V92 (simple-error V92))]
     [set | (lambda V93 (lambda V94 (set V93 V94)))]
     [str | (lambda V95 (str V95))]
     [reverse | (lambda V96 (reverse V96))]
     [remove | (lambda V97 (lambda V98 (remove V97 V98)))]
     [read | (lambda V99 (read V99))]
     [read-file | (lambda V100 (read-file V100))]
     [read-file-as-bytelist | (lambda V101 (read-file-as-bytelist V101))]
     [read-file-as-string | (lambda V102 (read-file-as-string V102))]
     [read-byte | (lambda V103 (read-byte V103))]
     [read-file-as-charlist | (lambda V104 (read-file-as-charlist V104))]
     [read-char-code | (lambda V105 (read-char-code V105))]
     [read-from-string | (lambda V106 (read-from-string V106))]
     [package? | (lambda V107 (package? V107))]
     [put | (lambda V108 (lambda V109 (lambda V110 (lambda V111 (put V108 V109 V110 V111)))))]
     [preclude | (lambda V112 (preclude V112))]
     [preclude-all-but | (lambda V113 (preclude-all-but V113))]
     [ps | (lambda V114 (ps V114))]
     [protect | (lambda V115 (protect V115))]
     [profile-results | (lambda V116 (profile-results V116))]
     [profile | (lambda V117 (profile V117))]
     [print | (lambda V118 (print V118))]
     [pr | (lambda V119 (lambda V120 (pr V119 V120)))]
     [pos | (lambda V121 (lambda V122 (pos V121 V122)))]
     [or | (lambda V123 (lambda V124 (or V123 V124)))]
     [optimise | (lambda V125 (optimise V125))]
     [open | (lambda V126 (lambda V127 (open V126 V127)))]
     [occurrences | (lambda V128 (lambda V129 (occurrences V128 V129)))]
     [occurs-check | (lambda V130 (occurs-check V130))]
     [n->string | (lambda V131 (n->string V131))]
     [number? | (lambda V132 (number? V132))]
     [nth | (lambda V133 (lambda V134 (nth V133 V134)))]
     [not | (lambda V135 (not V135))]
     [nl | (lambda V136 (nl V136))]
     [macroexpand | (lambda V137 (macroexpand V137))]
     [maxinferences | (lambda V138 (maxinferences V138))]
     [mapcan | (lambda V139 (lambda V140 (mapcan V139 V140)))]
     [map | (lambda V141 (lambda V142 (map V141 V142)))]
     [load | (lambda V143 (load V143))]
     [lineread | (lambda V144 (lineread V144))]
     [limit | (lambda V145 (limit V145))]
     [length | (lambda V146 (length V146))]
     [intersection | (lambda V147 (lambda V148 (intersection V147 V148)))]
     [intern | (lambda V149 (intern V149))]
     [integer? (lambda V150 (integer? V150))]
     [input | (lambda V151 (input V151))]
     [input+ | (lambda V152 (lambda V153 (input+ V152 V153)))]
     [include | (lambda V154 (include V154))]
     [include-all-but | (lambda V155 (include-all-but V155))]
     [internal | (lambda V156 (internal V156))]
     [if | (lambda V157 (lambda V158 (lambda V159 (if V157 V158 V159))))]
     [identical | (lambda V160 (lambda V161 (lambda V162 (lambda V163 (identical V160 V161 V162 V163)))))]
     [head | (lambda V164 (head V164))]
     [hd | (lambda V165 (hd V165))]
     [hdv | (lambda V166 (hdv V166))]
     [hdstr | (lambda V167 (hdstr V167))]
     [hash | (lambda V168 (lambda V169 (hash V168 V169)))]
     [get | (lambda V170 (lambda V171 (lambda V172 (get V170 V171 V172))))]
     [get/or | (lambda V173 (lambda V174 (lambda V175 (lambda V176 (get/or V173 V174 V175 V176)))))]
     [get-time | (lambda V177 (get-time V177))]
     [gensym | (lambda V178 (gensym V178))]
     [fst | (lambda V179 (fst V179))]
     [freeze | (lambda V180 (freeze V180))]
     [fix | (lambda V181 (lambda V182 (fix V181 V182)))]
     [fail-if | (lambda V183 (lambda V184 (fail-if V183 V184)))]
     [findall | (lambda V185 (lambda V186 (lambda V187 (lambda V188 (lambda V189 (findall V185 V186 V187 V188 V189))))))]
     [for-each | (lambda V190 (lambda V191 (for-each V190 V191)))]
     [fold-right | (lambda V192 (lambda V193 (lambda V194 (fold-right V192 V193 V194))))]
     [fold-left | (lambda V195 (lambda V196 (lambda V197 (fold-left V195 V196 V197))))]
     [filter | (lambda V198 (lambda V199 (filter V198 V199)))]
     [enable-type-theory | (lambda V200 (enable-type-theory V200))]
     [explode | (lambda V201 (explode V201))]
     [external | (lambda V202 (external V202))]
     [eval-kl | (lambda V203 (eval-kl V203))]
     [eval | (lambda V204 (eval V204))]
     [error-to-string | (lambda V205 (error-to-string V205))]
     [empty? | (lambda V206 (empty? V206))]
     [exit | (lambda V207 (exit V207))]
     [element? | (lambda V208 (lambda V209 (element? V208 V209)))]
     [do | (lambda V210 (lambda V211 (do V210 V211)))]
     [difference | (lambda V212 (lambda V213 (difference V212 V213)))]
     [destroy | (lambda V214 (destroy V214))]
     [declare | (lambda V215 (lambda V216 (declare V215 V216)))]
     [cn | (lambda V217 (lambda V218 (cn V217 V218)))]
     [cons? | (lambda V219 (cons? V219))]
     [cons | (lambda V220 (lambda V221 (cons V220 V221)))]
     [concat | (lambda V222 (lambda V223 (concat V222 V223)))]
     [compile | (lambda V224 (lambda V225 (lambda V226 (compile V224 V225 V226))))]
     [cd | (lambda V227 (cd V227))]
     [close | (lambda V228 (close V228))]
     [bound? | (lambda V229 (bound? V229))]
     [boolean? | (lambda V230 (boolean? V230))]
     [assoc | (lambda V231 (lambda V232 (assoc V231 V232)))]
     [arity | (lambda V233 (arity V233))]
     [append | (lambda V234 (lambda V235 (append V234 V235)))]
     [and | (lambda V236 (lambda V237 (and V236 V237)))]
     [adjoin | (lambda V238 (lambda V239 (adjoin V238 V239)))]
     [<-address | (lambda V240 (lambda V241 (<-address V240 V241)))]
     [<-address/or | (lambda V242 (lambda V243 (lambda V244 (<-address/or V242 V243 V244))))]
     [address-> | (lambda V245 (lambda V246 (lambda V247 (address-> V245 V246 V247))))]
     [absvector? | (lambda V248 (absvector? V248))]
     [absvector | (lambda V249 (absvector V249))]
     [dict | (lambda V250 (dict V250))]
     [dict? | (lambda V251 (dict? V251))]
     [dict-count | (lambda V252 (dict-count V252))]
     [dict-> | (lambda V253 (lambda V254 (lambda V255 (dict-> V253 V254 V255))))]
     [<-dict/or | (lambda V256 (lambda V257 (lambda V258 (<-dict/or V256 V257 V258))))]
     [<-dict | (lambda V259 (lambda V260 (<-dict V259 V260)))]
     [dict-rm | (lambda V261 (lambda V262 (dict-rm V261 V262)))]
     [dict-fold | (lambda V263 (lambda V264 (lambda V265 (dict-fold V263 V264 V265))))]
     [dict-keys | (lambda V266 (dict-keys V266))]
     [dict-values | (lambda V267 (dict-values V267))]]])

(define specialise
  F -> (do (set *special* [F | (value *special*)]) F))

(define unspecialise
  F -> (do (set *special* (remove F (value *special*))) F))

)
