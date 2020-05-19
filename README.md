<!-- omit in toc -->
# Astra
Astra is a Clojure framework for Model-Driven Software Development. You can read more about this in [the rationale section](#rationale).

<!-- omit in toc -->
# Table of Content
- [Installation](#installation)
- [Rationale](#rationale)
- [Features](#features)
- [License](#license)

## Installation

## Rationale
Models are simple representations of systems, and Model-Driven Software Development (MDSD) is a way to incorprate them into your normal workflow. The idea is that you build a model to represent your system, which can be used to address issues or design solutions that are translatable to your code base.

MDSD is not new, in fact it's as old as the (in)famous Unified Modeling Language (UML). However, despite its prevalence, UML rarely works well due to the dissynchronicity between the model and the code base. For example, adding a new class usually requires manual regeneration of a new UML model. Even worse, changes to the model itself are not propagated automatically to the code base with a manual trigger. Though this is just an example, it demonstrates a persisting problem with traditional models. One can of course argue that adding tooling can facilitate automatic translation, but it does not solve the problem, rather just migitate it.

MDSD aims to solve this by building a bridge between the code base and the model. Any changes to any side will be propagated automatically to the other side. This, in turn, ensures that both side are syntatically equivalent at all time.

There has been various libraries that aimed to this. Namely:
- JET
- Acceleo
- Epsilon

Despite their upsides, they are limitied to abstract and concrete models without the ease of modifying them. In other words, they allow creation of abstract models via their facility, from which concrete models can be created. The provided facility is good for most use cases, but sometimes their limitation really hinders the ability to go further. For example, sometimes run-time abstract model redefinition is required, but cannot be done via what those libraries provide.

This brings us to this library. Built on Clojure from the ground up, it's meant to replace the libraries above to further enhance the MDSD ecosystem. It's meant to close the loop between models and code base which allows one to abstractively define their models which are instantly translatable to their code base and vice versa. Input from models can be used for code base generation and vice versa. You can refer [here](#features) for more details.

## Features
- [ ] N-meta models
- [ ] Concrete models
- [ ] Code templates
- [ ] Code generation

## License
MIT License

Copyright (c) 2020 Rex Truong

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
