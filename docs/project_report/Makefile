short:
	@echo "Building LaTeX files"
	@mkdir .tmp
	@pdflatex -output-directory .tmp Report.tex
	@pdflatex -output-directory .tmp Report.tex
	@mv .tmp/Report.pdf Report.pdf
	
	@echo "Removing LaTeX build files"
	@rm -f *.out *.aux *.toc *.log *.lof *.bbl *.blg *.acn
	@rm -fr results/*.out results/*.aux *.toc *.log *.lof *.bbl *.blg *.acn
	@rm -fr .tmp/

clean:
	@echo "Removing LaTeX build files"
	@rm -f *.out *.aux *.toc *.log *.lof *.bbl *.blg *.acn
	@rm -fr results/*.out results/*.aux *.toc *.log *.lof *.bbl *.blg *.acn
	@rm -fr .tmp/

count:
	@echo "Counting words in Report"
	texcount -utf8 -total -inc -sum Report.tex
