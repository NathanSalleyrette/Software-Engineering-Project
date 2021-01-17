// Generated from DecaParser.g4 by ANTLR 4.4
package fr.ensimag.deca.syntax;

    import fr.ensimag.deca.tree.*;
    import fr.ensimag.deca.tools.SymbolTable;
    import java.io.PrintStream;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DecaParser}.
 */
public interface DecaParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DecaParser#list_inst}.
	 * @param ctx the parse tree
	 */
	void enterList_inst(@NotNull DecaParser.List_instContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#list_inst}.
	 * @param ctx the parse tree
	 */
	void exitList_inst(@NotNull DecaParser.List_instContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#assign_expr}.
	 * @param ctx the parse tree
	 */
	void enterAssign_expr(@NotNull DecaParser.Assign_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#assign_expr}.
	 * @param ctx the parse tree
	 */
	void exitAssign_expr(@NotNull DecaParser.Assign_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#ident}.
	 * @param ctx the parse tree
	 */
	void enterIdent(@NotNull DecaParser.IdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#ident}.
	 * @param ctx the parse tree
	 */
	void exitIdent(@NotNull DecaParser.IdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#select_expr}.
	 * @param ctx the parse tree
	 */
	void enterSelect_expr(@NotNull DecaParser.Select_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#select_expr}.
	 * @param ctx the parse tree
	 */
	void exitSelect_expr(@NotNull DecaParser.Select_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#main}.
	 * @param ctx the parse tree
	 */
	void enterMain(@NotNull DecaParser.MainContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#main}.
	 * @param ctx the parse tree
	 */
	void exitMain(@NotNull DecaParser.MainContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#decl_var}.
	 * @param ctx the parse tree
	 */
	void enterDecl_var(@NotNull DecaParser.Decl_varContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#decl_var}.
	 * @param ctx the parse tree
	 */
	void exitDecl_var(@NotNull DecaParser.Decl_varContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(@NotNull DecaParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(@NotNull DecaParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(@NotNull DecaParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(@NotNull DecaParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#and_expr}.
	 * @param ctx the parse tree
	 */
	void enterAnd_expr(@NotNull DecaParser.And_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#and_expr}.
	 * @param ctx the parse tree
	 */
	void exitAnd_expr(@NotNull DecaParser.And_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(@NotNull DecaParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(@NotNull DecaParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#if_then_else}.
	 * @param ctx the parse tree
	 */
	void enterIf_then_else(@NotNull DecaParser.If_then_elseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#if_then_else}.
	 * @param ctx the parse tree
	 */
	void exitIf_then_else(@NotNull DecaParser.If_then_elseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#or_expr}.
	 * @param ctx the parse tree
	 */
	void enterOr_expr(@NotNull DecaParser.Or_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#or_expr}.
	 * @param ctx the parse tree
	 */
	void exitOr_expr(@NotNull DecaParser.Or_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(@NotNull DecaParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(@NotNull DecaParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(@NotNull DecaParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(@NotNull DecaParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#primary_expr}.
	 * @param ctx the parse tree
	 */
	void enterPrimary_expr(@NotNull DecaParser.Primary_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#primary_expr}.
	 * @param ctx the parse tree
	 */
	void exitPrimary_expr(@NotNull DecaParser.Primary_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#class_extension}.
	 * @param ctx the parse tree
	 */
	void enterClass_extension(@NotNull DecaParser.Class_extensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#class_extension}.
	 * @param ctx the parse tree
	 */
	void exitClass_extension(@NotNull DecaParser.Class_extensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#multi_line_string}.
	 * @param ctx the parse tree
	 */
	void enterMulti_line_string(@NotNull DecaParser.Multi_line_stringContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#multi_line_string}.
	 * @param ctx the parse tree
	 */
	void exitMulti_line_string(@NotNull DecaParser.Multi_line_stringContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#sum_expr}.
	 * @param ctx the parse tree
	 */
	void enterSum_expr(@NotNull DecaParser.Sum_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#sum_expr}.
	 * @param ctx the parse tree
	 */
	void exitSum_expr(@NotNull DecaParser.Sum_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#class_body}.
	 * @param ctx the parse tree
	 */
	void enterClass_body(@NotNull DecaParser.Class_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#class_body}.
	 * @param ctx the parse tree
	 */
	void exitClass_body(@NotNull DecaParser.Class_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#visibility}.
	 * @param ctx the parse tree
	 */
	void enterVisibility(@NotNull DecaParser.VisibilityContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#visibility}.
	 * @param ctx the parse tree
	 */
	void exitVisibility(@NotNull DecaParser.VisibilityContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#inequality_expr}.
	 * @param ctx the parse tree
	 */
	void enterInequality_expr(@NotNull DecaParser.Inequality_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#inequality_expr}.
	 * @param ctx the parse tree
	 */
	void exitInequality_expr(@NotNull DecaParser.Inequality_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#list_decl}.
	 * @param ctx the parse tree
	 */
	void enterList_decl(@NotNull DecaParser.List_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#list_decl}.
	 * @param ctx the parse tree
	 */
	void exitList_decl(@NotNull DecaParser.List_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#decl_method}.
	 * @param ctx the parse tree
	 */
	void enterDecl_method(@NotNull DecaParser.Decl_methodContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#decl_method}.
	 * @param ctx the parse tree
	 */
	void exitDecl_method(@NotNull DecaParser.Decl_methodContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(@NotNull DecaParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(@NotNull DecaParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#mult_expr}.
	 * @param ctx the parse tree
	 */
	void enterMult_expr(@NotNull DecaParser.Mult_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#mult_expr}.
	 * @param ctx the parse tree
	 */
	void exitMult_expr(@NotNull DecaParser.Mult_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#unary_expr}.
	 * @param ctx the parse tree
	 */
	void enterUnary_expr(@NotNull DecaParser.Unary_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#unary_expr}.
	 * @param ctx the parse tree
	 */
	void exitUnary_expr(@NotNull DecaParser.Unary_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#eq_neq_expr}.
	 * @param ctx the parse tree
	 */
	void enterEq_neq_expr(@NotNull DecaParser.Eq_neq_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#eq_neq_expr}.
	 * @param ctx the parse tree
	 */
	void exitEq_neq_expr(@NotNull DecaParser.Eq_neq_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#list_decl_field}.
	 * @param ctx the parse tree
	 */
	void enterList_decl_field(@NotNull DecaParser.List_decl_fieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#list_decl_field}.
	 * @param ctx the parse tree
	 */
	void exitList_decl_field(@NotNull DecaParser.List_decl_fieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#list_expr}.
	 * @param ctx the parse tree
	 */
	void enterList_expr(@NotNull DecaParser.List_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#list_expr}.
	 * @param ctx the parse tree
	 */
	void exitList_expr(@NotNull DecaParser.List_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#list_classes}.
	 * @param ctx the parse tree
	 */
	void enterList_classes(@NotNull DecaParser.List_classesContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#list_classes}.
	 * @param ctx the parse tree
	 */
	void exitList_classes(@NotNull DecaParser.List_classesContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#decl_field}.
	 * @param ctx the parse tree
	 */
	void enterDecl_field(@NotNull DecaParser.Decl_fieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#decl_field}.
	 * @param ctx the parse tree
	 */
	void exitDecl_field(@NotNull DecaParser.Decl_fieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#list_decl_var}.
	 * @param ctx the parse tree
	 */
	void enterList_decl_var(@NotNull DecaParser.List_decl_varContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#list_decl_var}.
	 * @param ctx the parse tree
	 */
	void exitList_decl_var(@NotNull DecaParser.List_decl_varContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#inst}.
	 * @param ctx the parse tree
	 */
	void enterInst(@NotNull DecaParser.InstContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#inst}.
	 * @param ctx the parse tree
	 */
	void exitInst(@NotNull DecaParser.InstContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#decl_var_set}.
	 * @param ctx the parse tree
	 */
	void enterDecl_var_set(@NotNull DecaParser.Decl_var_setContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#decl_var_set}.
	 * @param ctx the parse tree
	 */
	void exitDecl_var_set(@NotNull DecaParser.Decl_var_setContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#class_decl}.
	 * @param ctx the parse tree
	 */
	void enterClass_decl(@NotNull DecaParser.Class_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#class_decl}.
	 * @param ctx the parse tree
	 */
	void exitClass_decl(@NotNull DecaParser.Class_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#decl_field_set}.
	 * @param ctx the parse tree
	 */
	void enterDecl_field_set(@NotNull DecaParser.Decl_field_setContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#decl_field_set}.
	 * @param ctx the parse tree
	 */
	void exitDecl_field_set(@NotNull DecaParser.Decl_field_setContext ctx);
	/**
	 * Enter a parse tree produced by {@link DecaParser#list_params}.
	 * @param ctx the parse tree
	 */
	void enterList_params(@NotNull DecaParser.List_paramsContext ctx);
	/**
	 * Exit a parse tree produced by {@link DecaParser#list_params}.
	 * @param ctx the parse tree
	 */
	void exitList_params(@NotNull DecaParser.List_paramsContext ctx);
}