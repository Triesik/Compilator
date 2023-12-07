package org.example.parser.context;

import org.example.visitor.Visitor;

public interface Visitable {

    Object accept(Visitor visitor);
}
