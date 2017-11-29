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

(define declare
  F A -> (let Record (set *signedfuncs* [[F | A] | (value *signedfuncs*)])
              Variancy (trap-error (variancy-test F A) (/. E skip))
              Type (rcons_form (demodulate A))
              F* (concat type-signature-of- F)
              Parameters (parameters 1)
              Clause [[F* (protect X)] :- [[unify! (protect X) Type]]]
              AUM_instruction (aum Clause Parameters)
              Code (aum_to_shen AUM_instruction)
              ShenDef [define F* | (append Parameters
                                           [(protect ProcessN) (protect Continuation)]
                                           [-> Code])]
              Eval (eval-without-macros ShenDef)
            F))

(define set-signedfuncs
  F A -> (set *signedfuncs* [[F | A] | (value *signedfuncs*)]))

(define demodulate
  X -> (let Demod (walk (value *demodulation-function*) X)
         (if (= Demod X)
             X
             (demodulate Demod))))

(define variancy-test
  F A -> (let TypeF (typecheck F (protect B))
              Check (cases (= symbol TypeF) skip
                           (variant? TypeF A) skip
                           true (output "warning: changing the type of ~A may create errors~%" F))
              skip))

(define variant?
  X X -> true
  [X | Y] [X | Z] -> (variant? Y Z)
  [X | Y] [W | Z] -> (variant? (subst a X Y) (subst a W Z))
      where (and (pvar? X) (variable? W))
  [[X | Y] | Z] [[X* | Y*] | Z*] -> (variant? (append [X | Y] Z)
                                              (append [X* | Y*] Z*))
  _ _ -> false)

(set-signedfuncs absvector? [A --> boolean])
(set-signedfuncs adjoin [A --> [[list A] --> [list A]]])
(set-signedfuncs and [boolean --> [boolean --> boolean]])
(set-signedfuncs app [A --> [string --> [symbol --> string]]])
(set-signedfuncs append [[list A] --> [[list A] --> [list A]]])
(set-signedfuncs arity [A --> number])
(set-signedfuncs assoc [A --> [[list [list A]] --> [list A]]])
(set-signedfuncs boolean? [A --> boolean])
(set-signedfuncs bound? [symbol --> boolean])
(set-signedfuncs cd [string --> string])
(set-signedfuncs close [[stream A] --> [list B]])
(set-signedfuncs cn [string --> [string --> string]])
(set-signedfuncs command-line [--> [list string]])
(set-signedfuncs compile [[A ==> B] --> [A --> [[A --> B] --> B]]])
(set-signedfuncs cons? [A --> boolean])
(set-signedfuncs destroy [[A --> B] --> symbol])
(set-signedfuncs difference [[list A] --> [[list A] --> [list A]]])
(set-signedfuncs do [A --> [B --> B]])
(set-signedfuncs <e> [[list A] ==> [list B]])
(set-signedfuncs <!> [[list A] ==> [list A]])
(set-signedfuncs element? [A --> [[list A] --> boolean]])
(set-signedfuncs empty? [A --> boolean])
(set-signedfuncs enable-type-theory [symbol --> boolean])
(set-signedfuncs external [symbol --> [list symbol]])
(set-signedfuncs error-to-string [exception --> string])
(set-signedfuncs explode [A --> [list string]])
(set-signedfuncs fail [--> symbol])
(set-signedfuncs fail-if [[symbol --> boolean] --> [symbol --> symbol]])
(set-signedfuncs fix [[A --> A] --> [A --> A]])
(set-signedfuncs freeze [A --> [lazy A]])
(set-signedfuncs fst [[A * B] --> A])
(set-signedfuncs function [[A --> B] --> [A --> B]])
(set-signedfuncs gensym [symbol --> symbol])
(set-signedfuncs <-vector [[vector A] --> [number --> A]])
(set-signedfuncs <-vector/or [[vector A] --> [number --> [[lazy A] --> A]]])
(set-signedfuncs vector-> [[vector A] --> [number --> [A --> [vector A]]]])
(set-signedfuncs vector [number --> [vector A]])
(set-signedfuncs dict [number --> [dict K V]])
(set-signedfuncs dict? [A --> boolean])
(set-signedfuncs dict-count [[dict K V] --> number])
(set-signedfuncs <-dict [[dict K V] --> [K --> V]])
(set-signedfuncs <-dict/or [[dict K V] --> [K --> [[lazy V] --> V]]])
(set-signedfuncs dict-> [[dict K V] --> [K --> [V --> V]]])
(set-signedfuncs dict-rm [[dict K V] --> [K --> K]])
(set-signedfuncs dict-fold [[K --> [V --> [A --> A]]] --> [[dict K V] --> [A --> A]]])
(set-signedfuncs dict-keys [[dict K V] --> [list K]])
(set-signedfuncs dict-values [[dict K V] --> [list V]])
(set-signedfuncs exit [number --> unit])
(set-signedfuncs get-time [symbol --> number])
(set-signedfuncs hash [A --> [number --> number]])
(set-signedfuncs head [[list A] --> A])
(set-signedfuncs hdv [[vector A] --> A])
(set-signedfuncs hdstr [string --> string])
(set-signedfuncs if [boolean --> [A --> [A --> A]]])
(set-signedfuncs it [--> string])
(set-signedfuncs implementation [--> string])
(set-signedfuncs include [[list symbol] --> [list symbol]])
(set-signedfuncs include-all-but [[list symbol] --> [list symbol]])
(set-signedfuncs inferences [--> number])
(set-signedfuncs insert [A --> [string --> string]])
(set-signedfuncs integer? [A --> boolean])
(set-signedfuncs internal [symbol --> [list symbol]])
(set-signedfuncs intersection [[list A] --> [[list A] --> [list A]]])
(set-signedfuncs kill [--> A])
(set-signedfuncs language [--> string])
(set-signedfuncs length [[list A] --> number])
(set-signedfuncs limit [[vector A] --> number])
(set-signedfuncs load [string --> symbol])
(set-signedfuncs fold-left [[B --> [A --> B]] --> [B --> [[list A] --> B]]])
(set-signedfuncs fold-right [[A --> [B --> B]] --> [[list A] --> [B --> B]]])
(set-signedfuncs for-each [[A --> B] --> [[list A] --> unit]])
(set-signedfuncs map [[A --> B] --> [[list A] --> [list B]]])
(set-signedfuncs mapcan [[A --> [list B]] --> [[list A] --> [list B]]])
(set-signedfuncs filter [[A --> boolean] --> [[list A] --> [list A]]])
(set-signedfuncs maxinferences [number --> number])
(set-signedfuncs n->string [number --> string])
(set-signedfuncs nl [number --> number])
(set-signedfuncs not [boolean --> boolean])
(set-signedfuncs nth [number --> [[list A] --> A]])
(set-signedfuncs number? [A --> boolean])
(set-signedfuncs occurrences [A --> [B --> number]])
(set-signedfuncs occurs-check [symbol --> boolean])
(set-signedfuncs optimise [symbol --> boolean])
(set-signedfuncs or [boolean --> [boolean --> boolean]])
(set-signedfuncs os [--> string])
(set-signedfuncs package? [symbol --> boolean])
(set-signedfuncs port [--> string])
(set-signedfuncs porters [--> string])
(set-signedfuncs pos [string --> [number --> string]])
(set-signedfuncs pr [string --> [[stream out] --> string]])
(set-signedfuncs print [A --> A])
(set-signedfuncs profile [[A --> B] --> [A --> B]])
(set-signedfuncs preclude [[list symbol] --> [list symbol]])
(set-signedfuncs proc-nl [string --> string])
(set-signedfuncs profile-results [[A --> B] --> [[A --> B] * number]])
(set-signedfuncs protect [symbol --> symbol])
(set-signedfuncs preclude-all-but [[list symbol] --> [list symbol]])
(set-signedfuncs prhush [string --> [[stream out] --> string]])
(set-signedfuncs ps [symbol --> [list unit]])
(set-signedfuncs read [[stream in] --> unit])
(set-signedfuncs read-byte [[stream in] --> number])
(set-signedfuncs read-char-code [[stream in] --> number])
(set-signedfuncs read-file-as-bytelist [string --> [list number]])
(set-signedfuncs read-file-as-charlist [string --> [list number]])
(set-signedfuncs read-file-as-string [string --> string])
(set-signedfuncs read-file [string --> [list unit]])
(set-signedfuncs read-from-string [string --> [list unit]])
(set-signedfuncs release [--> string])
(set-signedfuncs remove [A --> [[list A] --> [list A]]])
(set-signedfuncs reverse [[list A] --> [list A]])
(set-signedfuncs simple-error [string --> A])
(set-signedfuncs snd [[A * B] --> B])
(set-signedfuncs specialise [symbol --> symbol])
(set-signedfuncs spy [symbol --> boolean])
(set-signedfuncs step [symbol --> boolean])
(set-signedfuncs stinput [--> [stream in]])
(set-signedfuncs sterror [--> [stream out]])
(set-signedfuncs stoutput [--> [stream out]])
(set-signedfuncs string? [A --> boolean])
(set-signedfuncs str [A --> string])
(set-signedfuncs string->n [string --> number])
(set-signedfuncs string->symbol [string --> symbol])
(set-signedfuncs sum [[list number] --> number])
(set-signedfuncs symbol? [A --> boolean])
(set-signedfuncs systemf [symbol --> symbol])
(set-signedfuncs tail [[list A] --> [list A]])
(set-signedfuncs tlstr [string --> string])
(set-signedfuncs tlv [[vector A] --> [vector A]])
(set-signedfuncs tc [symbol --> boolean])
(set-signedfuncs tc? [--> boolean])
(set-signedfuncs thaw [[lazy A] --> A])
(set-signedfuncs track [symbol --> symbol])
(set-signedfuncs trap-error [A --> [[exception --> A] --> A]])
(set-signedfuncs tuple? [A --> boolean])
(set-signedfuncs undefmacro [symbol --> symbol])
(set-signedfuncs union [[list A] --> [[list A] --> [list A]]])
(set-signedfuncs unprofile [[A --> B] --> [A --> B]])
(set-signedfuncs untrack [symbol --> symbol])
(set-signedfuncs unspecialise [symbol --> symbol])
(set-signedfuncs variable? [A --> boolean])
(set-signedfuncs vector? [A --> boolean])
(set-signedfuncs version [--> string])
(set-signedfuncs write-to-file [string --> [A --> A]])
(set-signedfuncs write-byte [number --> [[stream out] --> number]])
(set-signedfuncs y-or-n? [string --> boolean])
(set-signedfuncs > [number --> [number --> boolean]])
(set-signedfuncs < [number --> [number --> boolean]])
(set-signedfuncs >= [number --> [number --> boolean]])
(set-signedfuncs <= [number --> [number --> boolean]])
(set-signedfuncs = [A --> [A --> boolean]])
(set-signedfuncs + [number --> [number --> number]])
(set-signedfuncs / [number --> [number --> number]])
(set-signedfuncs - [number --> [number --> number]])
(set-signedfuncs * [number --> [number --> number]])
(set-signedfuncs == [A --> [B --> boolean]])

(defun shen.type-signature-of-absvector? (V5 V6 V7) (let A (shen.newpv V6) (do (shen.incinfs) (unify! V5 (cons A (cons --> (cons boolean ()))) V6 V7))))
(defun shen.type-signature-of-adjoin (V12 V13 V14) (let A (shen.newpv V13) (do (shen.incinfs) (unify! V12 (cons A (cons --> (cons (cons (cons list (cons A ())) (cons --> (cons (cons list (cons A ())) ()))) ()))) V13 V14))))
(defun shen.type-signature-of-and (V19 V20 V21) (do (shen.incinfs) (unify! V19 (cons boolean (cons --> (cons (cons boolean (cons --> (cons boolean ()))) ()))) V20 V21)))
(defun shen.type-signature-of-shen.app (V26 V27 V28) (let A (shen.newpv V27) (do (shen.incinfs) (unify! V26 (cons A (cons --> (cons (cons string (cons --> (cons (cons symbol (cons --> (cons string ()))) ()))) ()))) V27 V28))))
(defun shen.type-signature-of-append (V33 V34 V35) (let A (shen.newpv V34) (do (shen.incinfs) (unify! V33 (cons (cons list (cons A ())) (cons --> (cons (cons (cons list (cons A ())) (cons --> (cons (cons list (cons A ())) ()))) ()))) V34 V35))))
(defun shen.type-signature-of-arity (V40 V41 V42) (let A (shen.newpv V41) (do (shen.incinfs) (unify! V40 (cons A (cons --> (cons number ()))) V41 V42))))
(defun shen.type-signature-of-assoc (V47 V48 V49) (let A (shen.newpv V48) (do (shen.incinfs) (unify! V47 (cons A (cons --> (cons (cons (cons list (cons (cons list (cons A ())) ())) (cons --> (cons (cons list (cons A ())) ()))) ()))) V48 V49))))
(defun shen.type-signature-of-boolean? (V54 V55 V56) (let A (shen.newpv V55) (do (shen.incinfs) (unify! V54 (cons A (cons --> (cons boolean ()))) V55 V56))))
(defun shen.type-signature-of-bound? (V61 V62 V63) (do (shen.incinfs) (unify! V61 (cons symbol (cons --> (cons boolean ()))) V62 V63)))
(defun shen.type-signature-of-cd (V68 V69 V70) (do (shen.incinfs) (unify! V68 (cons string (cons --> (cons string ()))) V69 V70)))
(defun shen.type-signature-of-close (V75 V76 V77) (let A (shen.newpv V76) (let B (shen.newpv V76) (do (shen.incinfs) (unify! V75 (cons (cons stream (cons A ())) (cons --> (cons (cons list (cons B ())) ()))) V76 V77)))))
(defun shen.type-signature-of-cn (V82 V83 V84) (do (shen.incinfs) (unify! V82 (cons string (cons --> (cons (cons string (cons --> (cons string ()))) ()))) V83 V84)))
(defun shen.type-signature-of-command-line (V89 V90 V91) (do (shen.incinfs) (unify! V89 (cons --> (cons (cons list (cons string ())) ())) V90 V91)))
(defun shen.type-signature-of-compile (V96 V97 V98) (let A (shen.newpv V97) (let B (shen.newpv V97) (do (shen.incinfs) (unify! V96 (cons (cons A (cons shen.==> (cons B ()))) (cons --> (cons (cons A (cons --> (cons (cons (cons A (cons --> (cons B ()))) (cons --> (cons B ()))) ()))) ()))) V97 V98)))))
(defun shen.type-signature-of-cons? (V103 V104 V105) (let A (shen.newpv V104) (do (shen.incinfs) (unify! V103 (cons A (cons --> (cons boolean ()))) V104 V105))))
(defun shen.type-signature-of-destroy (V110 V111 V112) (let A (shen.newpv V111) (let B (shen.newpv V111) (do (shen.incinfs) (unify! V110 (cons (cons A (cons --> (cons B ()))) (cons --> (cons symbol ()))) V111 V112)))))
(defun shen.type-signature-of-difference (V117 V118 V119) (let A (shen.newpv V118) (do (shen.incinfs) (unify! V117 (cons (cons list (cons A ())) (cons --> (cons (cons (cons list (cons A ())) (cons --> (cons (cons list (cons A ())) ()))) ()))) V118 V119))))
(defun shen.type-signature-of-do (V124 V125 V126) (let A (shen.newpv V125) (let B (shen.newpv V125) (do (shen.incinfs) (unify! V124 (cons A (cons --> (cons (cons B (cons --> (cons B ()))) ()))) V125 V126)))))
(defun shen.type-signature-of-<e> (V131 V132 V133) (let A (shen.newpv V132) (let B (shen.newpv V132) (do (shen.incinfs) (unify! V131 (cons (cons list (cons A ())) (cons shen.==> (cons (cons list (cons B ())) ()))) V132 V133)))))
(defun shen.type-signature-of-<!> (V138 V139 V140) (let A (shen.newpv V139) (do (shen.incinfs) (unify! V138 (cons (cons list (cons A ())) (cons shen.==> (cons (cons list (cons A ())) ()))) V139 V140))))
(defun shen.type-signature-of-element? (V145 V146 V147) (let A (shen.newpv V146) (do (shen.incinfs) (unify! V145 (cons A (cons --> (cons (cons (cons list (cons A ())) (cons --> (cons boolean ()))) ()))) V146 V147))))
(defun shen.type-signature-of-empty? (V152 V153 V154) (let A (shen.newpv V153) (do (shen.incinfs) (unify! V152 (cons A (cons --> (cons boolean ()))) V153 V154))))
(defun shen.type-signature-of-enable-type-theory (V159 V160 V161) (do (shen.incinfs) (unify! V159 (cons symbol (cons --> (cons boolean ()))) V160 V161)))
(defun shen.type-signature-of-external (V166 V167 V168) (do (shen.incinfs) (unify! V166 (cons symbol (cons --> (cons (cons list (cons symbol ())) ()))) V167 V168)))
(defun shen.type-signature-of-error-to-string (V173 V174 V175) (do (shen.incinfs) (unify! V173 (cons exception (cons --> (cons string ()))) V174 V175)))
(defun shen.type-signature-of-explode (V180 V181 V182) (let A (shen.newpv V181) (do (shen.incinfs) (unify! V180 (cons A (cons --> (cons (cons list (cons string ())) ()))) V181 V182))))
(defun shen.type-signature-of-fail (V187 V188 V189) (do (shen.incinfs) (unify! V187 (cons --> (cons symbol ())) V188 V189)))
(defun shen.type-signature-of-fail-if (V194 V195 V196) (do (shen.incinfs) (unify! V194 (cons (cons symbol (cons --> (cons boolean ()))) (cons --> (cons (cons symbol (cons --> (cons symbol ()))) ()))) V195 V196)))
(defun shen.type-signature-of-fix (V201 V202 V203) (let A (shen.newpv V202) (do (shen.incinfs) (unify! V201 (cons (cons A (cons --> (cons A ()))) (cons --> (cons (cons A (cons --> (cons A ()))) ()))) V202 V203))))
(defun shen.type-signature-of-freeze (V208 V209 V210) (let A (shen.newpv V209) (do (shen.incinfs) (unify! V208 (cons A (cons --> (cons (cons lazy (cons A ())) ()))) V209 V210))))
(defun shen.type-signature-of-fst (V215 V216 V217) (let B (shen.newpv V216) (let A (shen.newpv V216) (do (shen.incinfs) (unify! V215 (cons (cons A (cons * (cons B ()))) (cons --> (cons A ()))) V216 V217)))))
(defun shen.type-signature-of-function (V222 V223 V224) (let A (shen.newpv V223) (let B (shen.newpv V223) (do (shen.incinfs) (unify! V222 (cons (cons A (cons --> (cons B ()))) (cons --> (cons (cons A (cons --> (cons B ()))) ()))) V223 V224)))))
(defun shen.type-signature-of-gensym (V229 V230 V231) (do (shen.incinfs) (unify! V229 (cons symbol (cons --> (cons symbol ()))) V230 V231)))
(defun shen.type-signature-of-<-vector (V236 V237 V238) (let A (shen.newpv V237) (do (shen.incinfs) (unify! V236 (cons (cons vector (cons A ())) (cons --> (cons (cons number (cons --> (cons A ()))) ()))) V237 V238))))
(defun shen.type-signature-of-<-vector/or (V243 V244 V245) (let A (shen.newpv V244) (do (shen.incinfs) (unify! V243 (cons (cons vector (cons A ())) (cons --> (cons (cons number (cons --> (cons (cons (cons lazy (cons A ())) (cons --> (cons A ()))) ()))) ()))) V244 V245))))
(defun shen.type-signature-of-vector-> (V250 V251 V252) (let A (shen.newpv V251) (do (shen.incinfs) (unify! V250 (cons (cons vector (cons A ())) (cons --> (cons (cons number (cons --> (cons (cons A (cons --> (cons (cons vector (cons A ())) ()))) ()))) ()))) V251 V252))))
(defun shen.type-signature-of-vector (V257 V258 V259) (let A (shen.newpv V258) (do (shen.incinfs) (unify! V257 (cons number (cons --> (cons (cons vector (cons A ())) ()))) V258 V259))))
(defun shen.type-signature-of-dict (V264 V265 V266) (let K (shen.newpv V265) (let V (shen.newpv V265) (do (shen.incinfs) (unify! V264 (cons number (cons --> (cons (cons dict (cons K (cons V ()))) ()))) V265 V266)))))
(defun shen.type-signature-of-dict? (V271 V272 V273) (let A (shen.newpv V272) (do (shen.incinfs) (unify! V271 (cons A (cons --> (cons boolean ()))) V272 V273))))
(defun shen.type-signature-of-dict-count (V278 V279 V280) (let K (shen.newpv V279) (let V (shen.newpv V279) (do (shen.incinfs) (unify! V278 (cons (cons dict (cons K (cons V ()))) (cons --> (cons number ()))) V279 V280)))))
(defun shen.type-signature-of-<-dict (V285 V286 V287) (let K (shen.newpv V286) (let V (shen.newpv V286) (do (shen.incinfs) (unify! V285 (cons (cons dict (cons K (cons V ()))) (cons --> (cons (cons K (cons --> (cons V ()))) ()))) V286 V287)))))
(defun shen.type-signature-of-<-dict/or (V292 V293 V294) (let K (shen.newpv V293) (let V (shen.newpv V293) (do (shen.incinfs) (unify! V292 (cons (cons dict (cons K (cons V ()))) (cons --> (cons (cons K (cons --> (cons (cons (cons lazy (cons V ())) (cons --> (cons V ()))) ()))) ()))) V293 V294)))))
(defun shen.type-signature-of-dict-> (V299 V300 V301) (let K (shen.newpv V300) (let V (shen.newpv V300) (do (shen.incinfs) (unify! V299 (cons (cons dict (cons K (cons V ()))) (cons --> (cons (cons K (cons --> (cons (cons V (cons --> (cons V ()))) ()))) ()))) V300 V301)))))
(defun shen.type-signature-of-dict-rm (V306 V307 V308) (let V (shen.newpv V307) (let K (shen.newpv V307) (do (shen.incinfs) (unify! V306 (cons (cons dict (cons K (cons V ()))) (cons --> (cons (cons K (cons --> (cons K ()))) ()))) V307 V308)))))
(defun shen.type-signature-of-dict-fold (V313 V314 V315) (let K (shen.newpv V314) (let V (shen.newpv V314) (let A (shen.newpv V314) (do (shen.incinfs) (unify! V313 (cons (cons K (cons --> (cons (cons V (cons --> (cons (cons A (cons --> (cons A ()))) ()))) ()))) (cons --> (cons (cons (cons dict (cons K (cons V ()))) (cons --> (cons (cons A (cons --> (cons A ()))) ()))) ()))) V314 V315))))))
(defun shen.type-signature-of-dict-keys (V320 V321 V322) (let V (shen.newpv V321) (let K (shen.newpv V321) (do (shen.incinfs) (unify! V320 (cons (cons dict (cons K (cons V ()))) (cons --> (cons (cons list (cons K ())) ()))) V321 V322)))))
(defun shen.type-signature-of-dict-values (V327 V328 V329) (let K (shen.newpv V328) (let V (shen.newpv V328) (do (shen.incinfs) (unify! V327 (cons (cons dict (cons K (cons V ()))) (cons --> (cons (cons list (cons V ())) ()))) V328 V329)))))
(defun shen.type-signature-of-exit (V334 V335 V336) (do (shen.incinfs) (unify! V334 (cons number (cons --> (cons unit ()))) V335 V336)))
(defun shen.type-signature-of-get-time (V341 V342 V343) (do (shen.incinfs) (unify! V341 (cons symbol (cons --> (cons number ()))) V342 V343)))
(defun shen.type-signature-of-hash (V348 V349 V350) (let A (shen.newpv V349) (do (shen.incinfs) (unify! V348 (cons A (cons --> (cons (cons number (cons --> (cons number ()))) ()))) V349 V350))))
(defun shen.type-signature-of-head (V355 V356 V357) (let A (shen.newpv V356) (do (shen.incinfs) (unify! V355 (cons (cons list (cons A ())) (cons --> (cons A ()))) V356 V357))))
(defun shen.type-signature-of-hdv (V362 V363 V364) (let A (shen.newpv V363) (do (shen.incinfs) (unify! V362 (cons (cons vector (cons A ())) (cons --> (cons A ()))) V363 V364))))
(defun shen.type-signature-of-hdstr (V369 V370 V371) (do (shen.incinfs) (unify! V369 (cons string (cons --> (cons string ()))) V370 V371)))
(defun shen.type-signature-of-if (V376 V377 V378) (let A (shen.newpv V377) (do (shen.incinfs) (unify! V376 (cons boolean (cons --> (cons (cons A (cons --> (cons (cons A (cons --> (cons A ()))) ()))) ()))) V377 V378))))
(defun shen.type-signature-of-it (V383 V384 V385) (do (shen.incinfs) (unify! V383 (cons --> (cons string ())) V384 V385)))
(defun shen.type-signature-of-implementation (V390 V391 V392) (do (shen.incinfs) (unify! V390 (cons --> (cons string ())) V391 V392)))
(defun shen.type-signature-of-include (V397 V398 V399) (do (shen.incinfs) (unify! V397 (cons (cons list (cons symbol ())) (cons --> (cons (cons list (cons symbol ())) ()))) V398 V399)))
(defun shen.type-signature-of-include-all-but (V404 V405 V406) (do (shen.incinfs) (unify! V404 (cons (cons list (cons symbol ())) (cons --> (cons (cons list (cons symbol ())) ()))) V405 V406)))
(defun shen.type-signature-of-inferences (V411 V412 V413) (do (shen.incinfs) (unify! V411 (cons --> (cons number ())) V412 V413)))
(defun shen.type-signature-of-shen.insert (V418 V419 V420) (let A (shen.newpv V419) (do (shen.incinfs) (unify! V418 (cons A (cons --> (cons (cons string (cons --> (cons string ()))) ()))) V419 V420))))
(defun shen.type-signature-of-integer? (V425 V426 V427) (let A (shen.newpv V426) (do (shen.incinfs) (unify! V425 (cons A (cons --> (cons boolean ()))) V426 V427))))
(defun shen.type-signature-of-internal (V432 V433 V434) (do (shen.incinfs) (unify! V432 (cons symbol (cons --> (cons (cons list (cons symbol ())) ()))) V433 V434)))
(defun shen.type-signature-of-intersection (V439 V440 V441) (let A (shen.newpv V440) (do (shen.incinfs) (unify! V439 (cons (cons list (cons A ())) (cons --> (cons (cons (cons list (cons A ())) (cons --> (cons (cons list (cons A ())) ()))) ()))) V440 V441))))
(defun shen.type-signature-of-kill (V446 V447 V448) (let A (shen.newpv V447) (do (shen.incinfs) (unify! V446 (cons --> (cons A ())) V447 V448))))
(defun shen.type-signature-of-language (V453 V454 V455) (do (shen.incinfs) (unify! V453 (cons --> (cons string ())) V454 V455)))
(defun shen.type-signature-of-length (V460 V461 V462) (let A (shen.newpv V461) (do (shen.incinfs) (unify! V460 (cons (cons list (cons A ())) (cons --> (cons number ()))) V461 V462))))
(defun shen.type-signature-of-limit (V467 V468 V469) (let A (shen.newpv V468) (do (shen.incinfs) (unify! V467 (cons (cons vector (cons A ())) (cons --> (cons number ()))) V468 V469))))
(defun shen.type-signature-of-load (V474 V475 V476) (do (shen.incinfs) (unify! V474 (cons string (cons --> (cons symbol ()))) V475 V476)))
(defun shen.type-signature-of-fold-left (V481 V482 V483) (let A (shen.newpv V482) (let B (shen.newpv V482) (do (shen.incinfs) (unify! V481 (cons (cons B (cons --> (cons (cons A (cons --> (cons B ()))) ()))) (cons --> (cons (cons B (cons --> (cons (cons (cons list (cons A ())) (cons --> (cons B ()))) ()))) ()))) V482 V483)))))
(defun shen.type-signature-of-fold-right (V488 V489 V490) (let A (shen.newpv V489) (let B (shen.newpv V489) (do (shen.incinfs) (unify! V488 (cons (cons A (cons --> (cons (cons B (cons --> (cons B ()))) ()))) (cons --> (cons (cons (cons list (cons A ())) (cons --> (cons (cons B (cons --> (cons B ()))) ()))) ()))) V489 V490)))))
(defun shen.type-signature-of-for-each (V495 V496 V497) (let B (shen.newpv V496) (let A (shen.newpv V496) (do (shen.incinfs) (unify! V495 (cons (cons A (cons --> (cons B ()))) (cons --> (cons (cons (cons list (cons A ())) (cons --> (cons unit ()))) ()))) V496 V497)))))
(defun shen.type-signature-of-map (V502 V503 V504) (let A (shen.newpv V503) (let B (shen.newpv V503) (do (shen.incinfs) (unify! V502 (cons (cons A (cons --> (cons B ()))) (cons --> (cons (cons (cons list (cons A ())) (cons --> (cons (cons list (cons B ())) ()))) ()))) V503 V504)))))
(defun shen.type-signature-of-mapcan (V509 V510 V511) (let A (shen.newpv V510) (let B (shen.newpv V510) (do (shen.incinfs) (unify! V509 (cons (cons A (cons --> (cons (cons list (cons B ())) ()))) (cons --> (cons (cons (cons list (cons A ())) (cons --> (cons (cons list (cons B ())) ()))) ()))) V510 V511)))))
(defun shen.type-signature-of-filter (V516 V517 V518) (let A (shen.newpv V517) (do (shen.incinfs) (unify! V516 (cons (cons A (cons --> (cons boolean ()))) (cons --> (cons (cons (cons list (cons A ())) (cons --> (cons (cons list (cons A ())) ()))) ()))) V517 V518))))
(defun shen.type-signature-of-maxinferences (V523 V524 V525) (do (shen.incinfs) (unify! V523 (cons number (cons --> (cons number ()))) V524 V525)))
(defun shen.type-signature-of-n->string (V530 V531 V532) (do (shen.incinfs) (unify! V530 (cons number (cons --> (cons string ()))) V531 V532)))
(defun shen.type-signature-of-nl (V537 V538 V539) (do (shen.incinfs) (unify! V537 (cons number (cons --> (cons number ()))) V538 V539)))
(defun shen.type-signature-of-not (V544 V545 V546) (do (shen.incinfs) (unify! V544 (cons boolean (cons --> (cons boolean ()))) V545 V546)))
(defun shen.type-signature-of-nth (V551 V552 V553) (let A (shen.newpv V552) (do (shen.incinfs) (unify! V551 (cons number (cons --> (cons (cons (cons list (cons A ())) (cons --> (cons A ()))) ()))) V552 V553))))
(defun shen.type-signature-of-number? (V558 V559 V560) (let A (shen.newpv V559) (do (shen.incinfs) (unify! V558 (cons A (cons --> (cons boolean ()))) V559 V560))))
(defun shen.type-signature-of-occurrences (V565 V566 V567) (let A (shen.newpv V566) (let B (shen.newpv V566) (do (shen.incinfs) (unify! V565 (cons A (cons --> (cons (cons B (cons --> (cons number ()))) ()))) V566 V567)))))
(defun shen.type-signature-of-occurs-check (V572 V573 V574) (do (shen.incinfs) (unify! V572 (cons symbol (cons --> (cons boolean ()))) V573 V574)))
(defun shen.type-signature-of-optimise (V579 V580 V581) (do (shen.incinfs) (unify! V579 (cons symbol (cons --> (cons boolean ()))) V580 V581)))
(defun shen.type-signature-of-or (V586 V587 V588) (do (shen.incinfs) (unify! V586 (cons boolean (cons --> (cons (cons boolean (cons --> (cons boolean ()))) ()))) V587 V588)))
(defun shen.type-signature-of-os (V593 V594 V595) (do (shen.incinfs) (unify! V593 (cons --> (cons string ())) V594 V595)))
(defun shen.type-signature-of-package? (V600 V601 V602) (do (shen.incinfs) (unify! V600 (cons symbol (cons --> (cons boolean ()))) V601 V602)))
(defun shen.type-signature-of-port (V607 V608 V609) (do (shen.incinfs) (unify! V607 (cons --> (cons string ())) V608 V609)))
(defun shen.type-signature-of-porters (V614 V615 V616) (do (shen.incinfs) (unify! V614 (cons --> (cons string ())) V615 V616)))
(defun shen.type-signature-of-pos (V621 V622 V623) (do (shen.incinfs) (unify! V621 (cons string (cons --> (cons (cons number (cons --> (cons string ()))) ()))) V622 V623)))
(defun shen.type-signature-of-pr (V628 V629 V630) (do (shen.incinfs) (unify! V628 (cons string (cons --> (cons (cons (cons stream (cons out ())) (cons --> (cons string ()))) ()))) V629 V630)))
(defun shen.type-signature-of-print (V635 V636 V637) (let A (shen.newpv V636) (do (shen.incinfs) (unify! V635 (cons A (cons --> (cons A ()))) V636 V637))))
(defun shen.type-signature-of-profile (V642 V643 V644) (let A (shen.newpv V643) (let B (shen.newpv V643) (do (shen.incinfs) (unify! V642 (cons (cons A (cons --> (cons B ()))) (cons --> (cons (cons A (cons --> (cons B ()))) ()))) V643 V644)))))
(defun shen.type-signature-of-preclude (V649 V650 V651) (do (shen.incinfs) (unify! V649 (cons (cons list (cons symbol ())) (cons --> (cons (cons list (cons symbol ())) ()))) V650 V651)))
(defun shen.type-signature-of-shen.proc-nl (V656 V657 V658) (do (shen.incinfs) (unify! V656 (cons string (cons --> (cons string ()))) V657 V658)))
(defun shen.type-signature-of-profile-results (V663 V664 V665) (let A (shen.newpv V664) (let B (shen.newpv V664) (do (shen.incinfs) (unify! V663 (cons (cons A (cons --> (cons B ()))) (cons --> (cons (cons (cons A (cons --> (cons B ()))) (cons * (cons number ()))) ()))) V664 V665)))))
(defun shen.type-signature-of-protect (V670 V671 V672) (do (shen.incinfs) (unify! V670 (cons symbol (cons --> (cons symbol ()))) V671 V672)))
(defun shen.type-signature-of-preclude-all-but (V677 V678 V679) (do (shen.incinfs) (unify! V677 (cons (cons list (cons symbol ())) (cons --> (cons (cons list (cons symbol ())) ()))) V678 V679)))
(defun shen.type-signature-of-shen.prhush (V684 V685 V686) (do (shen.incinfs) (unify! V684 (cons string (cons --> (cons (cons (cons stream (cons out ())) (cons --> (cons string ()))) ()))) V685 V686)))
(defun shen.type-signature-of-ps (V691 V692 V693) (do (shen.incinfs) (unify! V691 (cons symbol (cons --> (cons (cons list (cons unit ())) ()))) V692 V693)))
(defun shen.type-signature-of-read (V698 V699 V700) (do (shen.incinfs) (unify! V698 (cons (cons stream (cons in ())) (cons --> (cons unit ()))) V699 V700)))
(defun shen.type-signature-of-read-byte (V705 V706 V707) (do (shen.incinfs) (unify! V705 (cons (cons stream (cons in ())) (cons --> (cons number ()))) V706 V707)))
(defun shen.type-signature-of-read-char-code (V712 V713 V714) (do (shen.incinfs) (unify! V712 (cons (cons stream (cons in ())) (cons --> (cons number ()))) V713 V714)))
(defun shen.type-signature-of-read-file-as-bytelist (V719 V720 V721) (do (shen.incinfs) (unify! V719 (cons string (cons --> (cons (cons list (cons number ())) ()))) V720 V721)))
(defun shen.type-signature-of-read-file-as-charlist (V726 V727 V728) (do (shen.incinfs) (unify! V726 (cons string (cons --> (cons (cons list (cons number ())) ()))) V727 V728)))
(defun shen.type-signature-of-read-file-as-string (V733 V734 V735) (do (shen.incinfs) (unify! V733 (cons string (cons --> (cons string ()))) V734 V735)))
(defun shen.type-signature-of-read-file (V740 V741 V742) (do (shen.incinfs) (unify! V740 (cons string (cons --> (cons (cons list (cons unit ())) ()))) V741 V742)))
(defun shen.type-signature-of-read-from-string (V747 V748 V749) (do (shen.incinfs) (unify! V747 (cons string (cons --> (cons (cons list (cons unit ())) ()))) V748 V749)))
(defun shen.type-signature-of-release (V754 V755 V756) (do (shen.incinfs) (unify! V754 (cons --> (cons string ())) V755 V756)))
(defun shen.type-signature-of-remove (V761 V762 V763) (let A (shen.newpv V762) (do (shen.incinfs) (unify! V761 (cons A (cons --> (cons (cons (cons list (cons A ())) (cons --> (cons (cons list (cons A ())) ()))) ()))) V762 V763))))
(defun shen.type-signature-of-reverse (V768 V769 V770) (let A (shen.newpv V769) (do (shen.incinfs) (unify! V768 (cons (cons list (cons A ())) (cons --> (cons (cons list (cons A ())) ()))) V769 V770))))
(defun shen.type-signature-of-simple-error (V775 V776 V777) (let A (shen.newpv V776) (do (shen.incinfs) (unify! V775 (cons string (cons --> (cons A ()))) V776 V777))))
(defun shen.type-signature-of-snd (V782 V783 V784) (let A (shen.newpv V783) (let B (shen.newpv V783) (do (shen.incinfs) (unify! V782 (cons (cons A (cons * (cons B ()))) (cons --> (cons B ()))) V783 V784)))))
(defun shen.type-signature-of-specialise (V789 V790 V791) (do (shen.incinfs) (unify! V789 (cons symbol (cons --> (cons symbol ()))) V790 V791)))
(defun shen.type-signature-of-spy (V796 V797 V798) (do (shen.incinfs) (unify! V796 (cons symbol (cons --> (cons boolean ()))) V797 V798)))
(defun shen.type-signature-of-step (V803 V804 V805) (do (shen.incinfs) (unify! V803 (cons symbol (cons --> (cons boolean ()))) V804 V805)))
(defun shen.type-signature-of-stinput (V810 V811 V812) (do (shen.incinfs) (unify! V810 (cons --> (cons (cons stream (cons in ())) ())) V811 V812)))
(defun shen.type-signature-of-sterror (V817 V818 V819) (do (shen.incinfs) (unify! V817 (cons --> (cons (cons stream (cons out ())) ())) V818 V819)))
(defun shen.type-signature-of-stoutput (V824 V825 V826) (do (shen.incinfs) (unify! V824 (cons --> (cons (cons stream (cons out ())) ())) V825 V826)))
(defun shen.type-signature-of-string? (V831 V832 V833) (let A (shen.newpv V832) (do (shen.incinfs) (unify! V831 (cons A (cons --> (cons boolean ()))) V832 V833))))
(defun shen.type-signature-of-str (V838 V839 V840) (let A (shen.newpv V839) (do (shen.incinfs) (unify! V838 (cons A (cons --> (cons string ()))) V839 V840))))
(defun shen.type-signature-of-string->n (V845 V846 V847) (do (shen.incinfs) (unify! V845 (cons string (cons --> (cons number ()))) V846 V847)))
(defun shen.type-signature-of-string->symbol (V852 V853 V854) (do (shen.incinfs) (unify! V852 (cons string (cons --> (cons symbol ()))) V853 V854)))
(defun shen.type-signature-of-sum (V859 V860 V861) (do (shen.incinfs) (unify! V859 (cons (cons list (cons number ())) (cons --> (cons number ()))) V860 V861)))
(defun shen.type-signature-of-symbol? (V866 V867 V868) (let A (shen.newpv V867) (do (shen.incinfs) (unify! V866 (cons A (cons --> (cons boolean ()))) V867 V868))))
(defun shen.type-signature-of-systemf (V873 V874 V875) (do (shen.incinfs) (unify! V873 (cons symbol (cons --> (cons symbol ()))) V874 V875)))
(defun shen.type-signature-of-tail (V880 V881 V882) (let A (shen.newpv V881) (do (shen.incinfs) (unify! V880 (cons (cons list (cons A ())) (cons --> (cons (cons list (cons A ())) ()))) V881 V882))))
(defun shen.type-signature-of-tlstr (V887 V888 V889) (do (shen.incinfs) (unify! V887 (cons string (cons --> (cons string ()))) V888 V889)))
(defun shen.type-signature-of-tlv (V894 V895 V896) (let A (shen.newpv V895) (do (shen.incinfs) (unify! V894 (cons (cons vector (cons A ())) (cons --> (cons (cons vector (cons A ())) ()))) V895 V896))))
(defun shen.type-signature-of-tc (V901 V902 V903) (do (shen.incinfs) (unify! V901 (cons symbol (cons --> (cons boolean ()))) V902 V903)))
(defun shen.type-signature-of-tc? (V908 V909 V910) (do (shen.incinfs) (unify! V908 (cons --> (cons boolean ())) V909 V910)))
(defun shen.type-signature-of-thaw (V915 V916 V917) (let A (shen.newpv V916) (do (shen.incinfs) (unify! V915 (cons (cons lazy (cons A ())) (cons --> (cons A ()))) V916 V917))))
(defun shen.type-signature-of-track (V922 V923 V924) (do (shen.incinfs) (unify! V922 (cons symbol (cons --> (cons symbol ()))) V923 V924)))
(defun shen.type-signature-of-trap-error (V929 V930 V931) (let A (shen.newpv V930) (do (shen.incinfs) (unify! V929 (cons A (cons --> (cons (cons (cons exception (cons --> (cons A ()))) (cons --> (cons A ()))) ()))) V930 V931))))
(defun shen.type-signature-of-tuple? (V936 V937 V938) (let A (shen.newpv V937) (do (shen.incinfs) (unify! V936 (cons A (cons --> (cons boolean ()))) V937 V938))))
(defun shen.type-signature-of-undefmacro (V943 V944 V945) (do (shen.incinfs) (unify! V943 (cons symbol (cons --> (cons symbol ()))) V944 V945)))
(defun shen.type-signature-of-union (V950 V951 V952) (let A (shen.newpv V951) (do (shen.incinfs) (unify! V950 (cons (cons list (cons A ())) (cons --> (cons (cons (cons list (cons A ())) (cons --> (cons (cons list (cons A ())) ()))) ()))) V951 V952))))
(defun shen.type-signature-of-unprofile (V957 V958 V959) (let A (shen.newpv V958) (let B (shen.newpv V958) (do (shen.incinfs) (unify! V957 (cons (cons A (cons --> (cons B ()))) (cons --> (cons (cons A (cons --> (cons B ()))) ()))) V958 V959)))))
(defun shen.type-signature-of-untrack (V964 V965 V966) (do (shen.incinfs) (unify! V964 (cons symbol (cons --> (cons symbol ()))) V965 V966)))
(defun shen.type-signature-of-unspecialise (V971 V972 V973) (do (shen.incinfs) (unify! V971 (cons symbol (cons --> (cons symbol ()))) V972 V973)))
(defun shen.type-signature-of-variable? (V978 V979 V980) (let A (shen.newpv V979) (do (shen.incinfs) (unify! V978 (cons A (cons --> (cons boolean ()))) V979 V980))))
(defun shen.type-signature-of-vector? (V985 V986 V987) (let A (shen.newpv V986) (do (shen.incinfs) (unify! V985 (cons A (cons --> (cons boolean ()))) V986 V987))))
(defun shen.type-signature-of-version (V992 V993 V994) (do (shen.incinfs) (unify! V992 (cons --> (cons string ())) V993 V994)))
(defun shen.type-signature-of-write-to-file (V999 V1000 V1001) (let A (shen.newpv V1000) (do (shen.incinfs) (unify! V999 (cons string (cons --> (cons (cons A (cons --> (cons A ()))) ()))) V1000 V1001))))
(defun shen.type-signature-of-write-byte (V1006 V1007 V1008) (do (shen.incinfs) (unify! V1006 (cons number (cons --> (cons (cons (cons stream (cons out ())) (cons --> (cons number ()))) ()))) V1007 V1008)))
(defun shen.type-signature-of-y-or-n? (V1013 V1014 V1015) (do (shen.incinfs) (unify! V1013 (cons string (cons --> (cons boolean ()))) V1014 V1015)))
(defun shen.type-signature-of-> (V1020 V1021 V1022) (do (shen.incinfs) (unify! V1020 (cons number (cons --> (cons (cons number (cons --> (cons boolean ()))) ()))) V1021 V1022)))
(defun shen.type-signature-of-< (V1027 V1028 V1029) (do (shen.incinfs) (unify! V1027 (cons number (cons --> (cons (cons number (cons --> (cons boolean ()))) ()))) V1028 V1029)))
(defun shen.type-signature-of->= (V1034 V1035 V1036) (do (shen.incinfs) (unify! V1034 (cons number (cons --> (cons (cons number (cons --> (cons boolean ()))) ()))) V1035 V1036)))
(defun shen.type-signature-of-<= (V1041 V1042 V1043) (do (shen.incinfs) (unify! V1041 (cons number (cons --> (cons (cons number (cons --> (cons boolean ()))) ()))) V1042 V1043)))
(defun shen.type-signature-of-= (V1048 V1049 V1050) (let A (shen.newpv V1049) (do (shen.incinfs) (unify! V1048 (cons A (cons --> (cons (cons A (cons --> (cons boolean ()))) ()))) V1049 V1050))))
(defun shen.type-signature-of-+ (V1055 V1056 V1057) (do (shen.incinfs) (unify! V1055 (cons number (cons --> (cons (cons number (cons --> (cons number ()))) ()))) V1056 V1057)))
(defun shen.type-signature-of-/ (V1062 V1063 V1064) (do (shen.incinfs) (unify! V1062 (cons number (cons --> (cons (cons number (cons --> (cons number ()))) ()))) V1063 V1064)))
(defun shen.type-signature-of-- (V1069 V1070 V1071) (do (shen.incinfs) (unify! V1069 (cons number (cons --> (cons (cons number (cons --> (cons number ()))) ()))) V1070 V1071)))
(defun shen.type-signature-of-* (V1076 V1077 V1078) (do (shen.incinfs) (unify! V1076 (cons number (cons --> (cons (cons number (cons --> (cons number ()))) ()))) V1077 V1078)))
(defun shen.type-signature-of-== (V1083 V1084 V1085) (let A (shen.newpv V1084) (let B (shen.newpv V1084) (do (shen.incinfs) (unify! V1083 (cons A (cons --> (cons (cons B (cons --> (cons boolean ()))) ()))) V1084 V1085)))))

(for-each
 (/. Entry (set-lambda-form-entry Entry))
 [[shen.type-signature-of-absvector? | (lambda V269 (lambda V270 (lambda V271 (shen.type-signature-of-absvector? V269 V270 V271))))]
  [shen.type-signature-of-adjoin | (lambda V276 (lambda V277 (lambda V278 (shen.type-signature-of-adjoin V276 V277 V278))))]
  [shen.type-signature-of-and | (lambda V283 (lambda V284 (lambda V285 (shen.type-signature-of-and V283 V284 V285))))]
  [shen.type-signature-of-shen.app | (lambda V290 (lambda V291 (lambda V292 (shen.type-signature-of-shen.app V290 V291 V292))))]
  [shen.type-signature-of-append | (lambda V297 (lambda V298 (lambda V299 (shen.type-signature-of-append V297 V298 V299))))]
  [shen.type-signature-of-arity | (lambda V304 (lambda V305 (lambda V306 (shen.type-signature-of-arity V304 V305 V306))))]
  [shen.type-signature-of-assoc | (lambda V311 (lambda V312 (lambda V313 (shen.type-signature-of-assoc V311 V312 V313))))]
  [shen.type-signature-of-boolean? | (lambda V318 (lambda V319 (lambda V320 (shen.type-signature-of-boolean? V318 V319 V320))))]
  [shen.type-signature-of-bound? | (lambda V325 (lambda V326 (lambda V327 (shen.type-signature-of-bound? V325 V326 V327))))]
  [shen.type-signature-of-cd | (lambda V332 (lambda V333 (lambda V334 (shen.type-signature-of-cd V332 V333 V334))))]
  [shen.type-signature-of-close | (lambda V339 (lambda V340 (lambda V341 (shen.type-signature-of-close V339 V340 V341))))]
  [shen.type-signature-of-cn | (lambda V346 (lambda V347 (lambda V348 (shen.type-signature-of-cn V346 V347 V348))))]
  [shen.type-signature-of-command-line | (lambda V353 (lambda V354 (lambda V355 (shen.type-signature-of-command-line V353 V354 V355))))]
  [shen.type-signature-of-compile | (lambda V360 (lambda V361 (lambda V362 (shen.type-signature-of-compile V360 V361 V362))))]
  [shen.type-signature-of-cons? | (lambda V367 (lambda V368 (lambda V369 (shen.type-signature-of-cons? V367 V368 V369))))]
  [shen.type-signature-of-destroy | (lambda V374 (lambda V375 (lambda V376 (shen.type-signature-of-destroy V374 V375 V376))))]
  [shen.type-signature-of-difference | (lambda V381 (lambda V382 (lambda V383 (shen.type-signature-of-difference V381 V382 V383))))]
  [shen.type-signature-of-do | (lambda V388 (lambda V389 (lambda V390 (shen.type-signature-of-do V388 V389 V390))))]
  [shen.type-signature-of-<e> | (lambda V395 (lambda V396 (lambda V397 (shen.type-signature-of-<e> V395 V396 V397))))]
  [shen.type-signature-of-<!> | (lambda V402 (lambda V403 (lambda V404 (shen.type-signature-of-<!> V402 V403 V404))))]
  [shen.type-signature-of-element? | (lambda V409 (lambda V410 (lambda V411 (shen.type-signature-of-element? V409 V410 V411))))]
  [shen.type-signature-of-empty? | (lambda V416 (lambda V417 (lambda V418 (shen.type-signature-of-empty? V416 V417 V418))))]
  [shen.type-signature-of-enable-type-theory | (lambda V423 (lambda V424 (lambda V425 (shen.type-signature-of-enable-type-theory V423 V424 V425))))]
  [shen.type-signature-of-external | (lambda V430 (lambda V431 (lambda V432 (shen.type-signature-of-external V430 V431 V432))))]
  [shen.type-signature-of-error-to-string | (lambda V437 (lambda V438 (lambda V439 (shen.type-signature-of-error-to-string V437 V438 V439))))]
  [shen.type-signature-of-explode | (lambda V444 (lambda V445 (lambda V446 (shen.type-signature-of-explode V444 V445 V446))))]
  [shen.type-signature-of-fail | (lambda V451 (lambda V452 (lambda V453 (shen.type-signature-of-fail V451 V452 V453))))]
  [shen.type-signature-of-fail-if | (lambda V458 (lambda V459 (lambda V460 (shen.type-signature-of-fail-if V458 V459 V460))))]
  [shen.type-signature-of-fix | (lambda V465 (lambda V466 (lambda V467 (shen.type-signature-of-fix V465 V466 V467))))]
  [shen.type-signature-of-freeze | (lambda V472 (lambda V473 (lambda V474 (shen.type-signature-of-freeze V472 V473 V474))))]
  [shen.type-signature-of-fst | (lambda V479 (lambda V480 (lambda V481 (shen.type-signature-of-fst V479 V480 V481))))]
  [shen.type-signature-of-function | (lambda V486 (lambda V487 (lambda V488 (shen.type-signature-of-function V486 V487 V488))))]
  [shen.type-signature-of-gensym | (lambda V493 (lambda V494 (lambda V495 (shen.type-signature-of-gensym V493 V494 V495))))]
  [shen.type-signature-of-<-vector | (lambda V500 (lambda V501 (lambda V502 (shen.type-signature-of-<-vector V500 V501 V502))))]
  [shen.type-signature-of-<-vector/or | (lambda V507 (lambda V508 (lambda V509 (shen.type-signature-of-<-vector/or V507 V508 V509))))]
  [shen.type-signature-of-vector-> | (lambda V514 (lambda V515 (lambda V516 (shen.type-signature-of-vector-> V514 V515 V516))))]
  [shen.type-signature-of-vector | (lambda V521 (lambda V522 (lambda V523 (shen.type-signature-of-vector V521 V522 V523))))]
  [shen.type-signature-of-dict | (lambda V528 (lambda V529 (lambda V530 (shen.type-signature-of-dict V528 V529 V530))))]
  [shen.type-signature-of-dict? | (lambda V535 (lambda V536 (lambda V537 (shen.type-signature-of-dict? V535 V536 V537))))]
  [shen.type-signature-of-dict-count | (lambda V542 (lambda V543 (lambda V544 (shen.type-signature-of-dict-count V542 V543 V544))))]
  [shen.type-signature-of-<-dict | (lambda V549 (lambda V550 (lambda V551 (shen.type-signature-of-<-dict V549 V550 V551))))]
  [shen.type-signature-of-<-dict/or | (lambda V556 (lambda V557 (lambda V558 (shen.type-signature-of-<-dict/or V556 V557 V558))))]
  [shen.type-signature-of-dict-> | (lambda V563 (lambda V564 (lambda V565 (shen.type-signature-of-dict-> V563 V564 V565))))]
  [shen.type-signature-of-dict-rm | (lambda V570 (lambda V571 (lambda V572 (shen.type-signature-of-dict-rm V570 V571 V572))))]
  [shen.type-signature-of-dict-fold | (lambda V577 (lambda V578 (lambda V579 (shen.type-signature-of-dict-fold V577 V578 V579))))]
  [shen.type-signature-of-dict-keys | (lambda V584 (lambda V585 (lambda V586 (shen.type-signature-of-dict-keys V584 V585 V586))))]
  [shen.type-signature-of-dict-values | (lambda V591 (lambda V592 (lambda V593 (shen.type-signature-of-dict-values V591 V592 V593))))]
  [shen.type-signature-of-exit | (lambda V598 (lambda V599 (lambda V600 (shen.type-signature-of-exit V598 V599 V600))))]
  [shen.type-signature-of-get-time | (lambda V605 (lambda V606 (lambda V607 (shen.type-signature-of-get-time V605 V606 V607))))]
  [shen.type-signature-of-hash | (lambda V612 (lambda V613 (lambda V614 (shen.type-signature-of-hash V612 V613 V614))))]
  [shen.type-signature-of-head | (lambda V619 (lambda V620 (lambda V621 (shen.type-signature-of-head V619 V620 V621))))]
  [shen.type-signature-of-hdv | (lambda V626 (lambda V627 (lambda V628 (shen.type-signature-of-hdv V626 V627 V628))))]
  [shen.type-signature-of-hdstr | (lambda V633 (lambda V634 (lambda V635 (shen.type-signature-of-hdstr V633 V634 V635))))]
  [shen.type-signature-of-if | (lambda V640 (lambda V641 (lambda V642 (shen.type-signature-of-if V640 V641 V642))))]
  [shen.type-signature-of-it | (lambda V647 (lambda V648 (lambda V649 (shen.type-signature-of-it V647 V648 V649))))]
  [shen.type-signature-of-implementation | (lambda V654 (lambda V655 (lambda V656 (shen.type-signature-of-implementation V654 V655 V656))))]
  [shen.type-signature-of-include | (lambda V661 (lambda V662 (lambda V663 (shen.type-signature-of-include V661 V662 V663))))]
  [shen.type-signature-of-include-all-but | (lambda V668 (lambda V669 (lambda V670 (shen.type-signature-of-include-all-but V668 V669 V670))))]
  [shen.type-signature-of-inferences | (lambda V675 (lambda V676 (lambda V677 (shen.type-signature-of-inferences V675 V676 V677))))]
  [shen.type-signature-of-shen.insert | (lambda V682 (lambda V683 (lambda V684 (shen.type-signature-of-shen.insert V682 V683 V684))))]
  [shen.type-signature-of-integer? | (lambda V689 (lambda V690 (lambda V691 (shen.type-signature-of-integer? V689 V690 V691))))]
  [shen.type-signature-of-internal | (lambda V696 (lambda V697 (lambda V698 (shen.type-signature-of-internal V696 V697 V698))))]
  [shen.type-signature-of-intersection | (lambda V703 (lambda V704 (lambda V705 (shen.type-signature-of-intersection V703 V704 V705))))]
  [shen.type-signature-of-kill | (lambda V710 (lambda V711 (lambda V712 (shen.type-signature-of-kill V710 V711 V712))))]
  [shen.type-signature-of-language | (lambda V717 (lambda V718 (lambda V719 (shen.type-signature-of-language V717 V718 V719))))]
  [shen.type-signature-of-length | (lambda V724 (lambda V725 (lambda V726 (shen.type-signature-of-length V724 V725 V726))))]
  [shen.type-signature-of-limit | (lambda V731 (lambda V732 (lambda V733 (shen.type-signature-of-limit V731 V732 V733))))]
  [shen.type-signature-of-load | (lambda V738 (lambda V739 (lambda V740 (shen.type-signature-of-load V738 V739 V740))))]
  [shen.type-signature-of-fold-left | (lambda V745 (lambda V746 (lambda V747 (shen.type-signature-of-fold-left V745 V746 V747))))]
  [shen.type-signature-of-fold-right | (lambda V752 (lambda V753 (lambda V754 (shen.type-signature-of-fold-right V752 V753 V754))))]
  [shen.type-signature-of-for-each | (lambda V759 (lambda V760 (lambda V761 (shen.type-signature-of-for-each V759 V760 V761))))]
  [shen.type-signature-of-map | (lambda V766 (lambda V767 (lambda V768 (shen.type-signature-of-map V766 V767 V768))))]
  [shen.type-signature-of-mapcan | (lambda V773 (lambda V774 (lambda V775 (shen.type-signature-of-mapcan V773 V774 V775))))]
  [shen.type-signature-of-filter | (lambda V780 (lambda V781 (lambda V782 (shen.type-signature-of-filter V780 V781 V782))))]
  [shen.type-signature-of-maxinferences | (lambda V787 (lambda V788 (lambda V789 (shen.type-signature-of-maxinferences V787 V788 V789))))]
  [shen.type-signature-of-n->string | (lambda V794 (lambda V795 (lambda V796 (shen.type-signature-of-n->string V794 V795 V796))))]
  [shen.type-signature-of-nl | (lambda V801 (lambda V802 (lambda V803 (shen.type-signature-of-nl V801 V802 V803))))]
  [shen.type-signature-of-not | (lambda V808 (lambda V809 (lambda V810 (shen.type-signature-of-not V808 V809 V810))))]
  [shen.type-signature-of-nth | (lambda V815 (lambda V816 (lambda V817 (shen.type-signature-of-nth V815 V816 V817))))]
  [shen.type-signature-of-number? | (lambda V822 (lambda V823 (lambda V824 (shen.type-signature-of-number? V822 V823 V824))))]
  [shen.type-signature-of-occurrences | (lambda V829 (lambda V830 (lambda V831 (shen.type-signature-of-occurrences V829 V830 V831))))]
  [shen.type-signature-of-occurs-check | (lambda V836 (lambda V837 (lambda V838 (shen.type-signature-of-occurs-check V836 V837 V838))))]
  [shen.type-signature-of-optimise | (lambda V843 (lambda V844 (lambda V845 (shen.type-signature-of-optimise V843 V844 V845))))]
  [shen.type-signature-of-or | (lambda V850 (lambda V851 (lambda V852 (shen.type-signature-of-or V850 V851 V852))))]
  [shen.type-signature-of-os | (lambda V857 (lambda V858 (lambda V859 (shen.type-signature-of-os V857 V858 V859))))]
  [shen.type-signature-of-package? | (lambda V864 (lambda V865 (lambda V866 (shen.type-signature-of-package? V864 V865 V866))))]
  [shen.type-signature-of-port | (lambda V871 (lambda V872 (lambda V873 (shen.type-signature-of-port V871 V872 V873))))]
  [shen.type-signature-of-porters | (lambda V878 (lambda V879 (lambda V880 (shen.type-signature-of-porters V878 V879 V880))))]
  [shen.type-signature-of-pos | (lambda V885 (lambda V886 (lambda V887 (shen.type-signature-of-pos V885 V886 V887))))]
  [shen.type-signature-of-pr | (lambda V892 (lambda V893 (lambda V894 (shen.type-signature-of-pr V892 V893 V894))))]
  [shen.type-signature-of-print | (lambda V899 (lambda V900 (lambda V901 (shen.type-signature-of-print V899 V900 V901))))]
  [shen.type-signature-of-profile | (lambda V906 (lambda V907 (lambda V908 (shen.type-signature-of-profile V906 V907 V908))))]
  [shen.type-signature-of-preclude | (lambda V913 (lambda V914 (lambda V915 (shen.type-signature-of-preclude V913 V914 V915))))]
  [shen.type-signature-of-shen.proc-nl | (lambda V920 (lambda V921 (lambda V922 (shen.type-signature-of-shen.proc-nl V920 V921 V922))))]
  [shen.type-signature-of-profile-results | (lambda V927 (lambda V928 (lambda V929 (shen.type-signature-of-profile-results V927 V928 V929))))]
  [shen.type-signature-of-protect | (lambda V934 (lambda V935 (lambda V936 (shen.type-signature-of-protect V934 V935 V936))))]
  [shen.type-signature-of-preclude-all-but | (lambda V941 (lambda V942 (lambda V943 (shen.type-signature-of-preclude-all-but V941 V942 V943))))]
  [shen.type-signature-of-shen.prhush | (lambda V948 (lambda V949 (lambda V950 (shen.type-signature-of-shen.prhush V948 V949 V950))))]
  [shen.type-signature-of-ps | (lambda V955 (lambda V956 (lambda V957 (shen.type-signature-of-ps V955 V956 V957))))]
  [shen.type-signature-of-read | (lambda V962 (lambda V963 (lambda V964 (shen.type-signature-of-read V962 V963 V964))))]
  [shen.type-signature-of-read-byte | (lambda V969 (lambda V970 (lambda V971 (shen.type-signature-of-read-byte V969 V970 V971))))]
  [shen.type-signature-of-read-char-code | (lambda V976 (lambda V977 (lambda V978 (shen.type-signature-of-read-char-code V976 V977 V978))))]
  [shen.type-signature-of-read-file-as-bytelist | (lambda V983 (lambda V984 (lambda V985 (shen.type-signature-of-read-file-as-bytelist V983 V984 V985))))]
  [shen.type-signature-of-read-file-as-charlist | (lambda V990 (lambda V991 (lambda V992 (shen.type-signature-of-read-file-as-charlist V990 V991 V992))))]
  [shen.type-signature-of-read-file-as-string | (lambda V997 (lambda V998 (lambda V999 (shen.type-signature-of-read-file-as-string V997 V998 V999))))]
  [shen.type-signature-of-read-file | (lambda V1004 (lambda V1005 (lambda V1006 (shen.type-signature-of-read-file V1004 V1005 V1006))))]
  [shen.type-signature-of-read-from-string | (lambda V1011 (lambda V1012 (lambda V1013 (shen.type-signature-of-read-from-string V1011 V1012 V1013))))]
  [shen.type-signature-of-release | (lambda V1018 (lambda V1019 (lambda V1020 (shen.type-signature-of-release V1018 V1019 V1020))))]
  [shen.type-signature-of-remove | (lambda V1025 (lambda V1026 (lambda V1027 (shen.type-signature-of-remove V1025 V1026 V1027))))]
  [shen.type-signature-of-reverse | (lambda V1032 (lambda V1033 (lambda V1034 (shen.type-signature-of-reverse V1032 V1033 V1034))))]
  [shen.type-signature-of-simple-error | (lambda V1039 (lambda V1040 (lambda V1041 (shen.type-signature-of-simple-error V1039 V1040 V1041))))]
  [shen.type-signature-of-snd | (lambda V1046 (lambda V1047 (lambda V1048 (shen.type-signature-of-snd V1046 V1047 V1048))))]
  [shen.type-signature-of-specialise | (lambda V1053 (lambda V1054 (lambda V1055 (shen.type-signature-of-specialise V1053 V1054 V1055))))]
  [shen.type-signature-of-spy | (lambda V1060 (lambda V1061 (lambda V1062 (shen.type-signature-of-spy V1060 V1061 V1062))))]
  [shen.type-signature-of-step | (lambda V1067 (lambda V1068 (lambda V1069 (shen.type-signature-of-step V1067 V1068 V1069))))]
  [shen.type-signature-of-stinput | (lambda V1074 (lambda V1075 (lambda V1076 (shen.type-signature-of-stinput V1074 V1075 V1076))))]
  [shen.type-signature-of-sterror | (lambda V1081 (lambda V1082 (lambda V1083 (shen.type-signature-of-sterror V1081 V1082 V1083))))]
  [shen.type-signature-of-stoutput | (lambda V1088 (lambda V1089 (lambda V1090 (shen.type-signature-of-stoutput V1088 V1089 V1090))))]
  [shen.type-signature-of-string? | (lambda V1095 (lambda V1096 (lambda V1097 (shen.type-signature-of-string? V1095 V1096 V1097))))]
  [shen.type-signature-of-str | (lambda V1102 (lambda V1103 (lambda V1104 (shen.type-signature-of-str V1102 V1103 V1104))))]
  [shen.type-signature-of-string->n | (lambda V1109 (lambda V1110 (lambda V1111 (shen.type-signature-of-string->n V1109 V1110 V1111))))]
  [shen.type-signature-of-string->symbol | (lambda V1116 (lambda V1117 (lambda V1118 (shen.type-signature-of-string->symbol V1116 V1117 V1118))))]
  [shen.type-signature-of-sum | (lambda V1123 (lambda V1124 (lambda V1125 (shen.type-signature-of-sum V1123 V1124 V1125))))]
  [shen.type-signature-of-symbol? | (lambda V1130 (lambda V1131 (lambda V1132 (shen.type-signature-of-symbol? V1130 V1131 V1132))))]
  [shen.type-signature-of-systemf | (lambda V1137 (lambda V1138 (lambda V1139 (shen.type-signature-of-systemf V1137 V1138 V1139))))]
  [shen.type-signature-of-tail | (lambda V1144 (lambda V1145 (lambda V1146 (shen.type-signature-of-tail V1144 V1145 V1146))))]
  [shen.type-signature-of-tlstr | (lambda V1151 (lambda V1152 (lambda V1153 (shen.type-signature-of-tlstr V1151 V1152 V1153))))]
  [shen.type-signature-of-tlv | (lambda V1158 (lambda V1159 (lambda V1160 (shen.type-signature-of-tlv V1158 V1159 V1160))))]
  [shen.type-signature-of-tc | (lambda V1165 (lambda V1166 (lambda V1167 (shen.type-signature-of-tc V1165 V1166 V1167))))]
  [shen.type-signature-of-tc? | (lambda V1172 (lambda V1173 (lambda V1174 (shen.type-signature-of-tc? V1172 V1173 V1174))))]
  [shen.type-signature-of-thaw | (lambda V1179 (lambda V1180 (lambda V1181 (shen.type-signature-of-thaw V1179 V1180 V1181))))]
  [shen.type-signature-of-track | (lambda V1186 (lambda V1187 (lambda V1188 (shen.type-signature-of-track V1186 V1187 V1188))))]
  [shen.type-signature-of-trap-error | (lambda V1193 (lambda V1194 (lambda V1195 (shen.type-signature-of-trap-error V1193 V1194 V1195))))]
  [shen.type-signature-of-tuple? | (lambda V1200 (lambda V1201 (lambda V1202 (shen.type-signature-of-tuple? V1200 V1201 V1202))))]
  [shen.type-signature-of-undefmacro | (lambda V1207 (lambda V1208 (lambda V1209 (shen.type-signature-of-undefmacro V1207 V1208 V1209))))]
  [shen.type-signature-of-union | (lambda V1214 (lambda V1215 (lambda V1216 (shen.type-signature-of-union V1214 V1215 V1216))))]
  [shen.type-signature-of-unprofile | (lambda V1221 (lambda V1222 (lambda V1223 (shen.type-signature-of-unprofile V1221 V1222 V1223))))]
  [shen.type-signature-of-untrack | (lambda V1228 (lambda V1229 (lambda V1230 (shen.type-signature-of-untrack V1228 V1229 V1230))))]
  [shen.type-signature-of-unspecialise | (lambda V1235 (lambda V1236 (lambda V1237 (shen.type-signature-of-unspecialise V1235 V1236 V1237))))]
  [shen.type-signature-of-variable? | (lambda V1242 (lambda V1243 (lambda V1244 (shen.type-signature-of-variable? V1242 V1243 V1244))))]
  [shen.type-signature-of-vector? | (lambda V1249 (lambda V1250 (lambda V1251 (shen.type-signature-of-vector? V1249 V1250 V1251))))]
  [shen.type-signature-of-version | (lambda V1256 (lambda V1257 (lambda V1258 (shen.type-signature-of-version V1256 V1257 V1258))))]
  [shen.type-signature-of-write-to-file | (lambda V1263 (lambda V1264 (lambda V1265 (shen.type-signature-of-write-to-file V1263 V1264 V1265))))]
  [shen.type-signature-of-write-byte | (lambda V1270 (lambda V1271 (lambda V1272 (shen.type-signature-of-write-byte V1270 V1271 V1272))))]
  [shen.type-signature-of-y-or-n? | (lambda V1277 (lambda V1278 (lambda V1279 (shen.type-signature-of-y-or-n? V1277 V1278 V1279))))]
  [shen.type-signature-of-> | (lambda V1284 (lambda V1285 (lambda V1286 (shen.type-signature-of-> V1284 V1285 V1286))))]
  [shen.type-signature-of-< | (lambda V1291 (lambda V1292 (lambda V1293 (shen.type-signature-of-< V1291 V1292 V1293))))]
  [shen.type-signature-of->= | (lambda V1298 (lambda V1299 (lambda V1300 (shen.type-signature-of->= V1298 V1299 V1300))))]
  [shen.type-signature-of-<= | (lambda V1305 (lambda V1306 (lambda V1307 (shen.type-signature-of-<= V1305 V1306 V1307))))]
  [shen.type-signature-of-= | (lambda V1312 (lambda V1313 (lambda V1314 (shen.type-signature-of-= V1312 V1313 V1314))))]
  [shen.type-signature-of-+ | (lambda V1319 (lambda V1320 (lambda V1321 (shen.type-signature-of-+ V1319 V1320 V1321))))]
  [shen.type-signature-of-/ | (lambda V1326 (lambda V1327 (lambda V1328 (shen.type-signature-of-/ V1326 V1327 V1328))))]
  [shen.type-signature-of-- | (lambda V1333 (lambda V1334 (lambda V1335 (shen.type-signature-of-- V1333 V1334 V1335))))]
  [shen.type-signature-of-* | (lambda V1340 (lambda V1341 (lambda V1342 (shen.type-signature-of-* V1340 V1341 V1342))))]
  [shen.type-signature-of-== | (lambda V1347 (lambda V1348 (lambda V1349 (shen.type-signature-of-== V1347 V1348 V1349))))]]))
