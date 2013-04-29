;------------------------------------------
; Shows all files in the "Setups" folder with
; the ending .phS" and opens the choosen one.
;------------------------------------------



Local Files$[100]
Local Dir = ReadDir("Setups")
Local File$
Local FileNr = 0
Repeat 
	File$ = NextFile$(Dir) 
	If File$ = "" Then Exit 
	If FileType("Setups\" + File$) <> 2 And Right(File$,4) = ".phS" Then 
		Files[FileNr] = Left(File$, Instr(File$,".phS")-1 )
		FileNr=FileNr+1
	End If 
Forever 


Local font = LoadFont("Arial",25,1) 
SetFont font
Repeat
	Local i
	Cls
	For i=0 To FileNr-1
		Text 20, i*30 + MouseZ()*10, Files[i]
	Next
	
	If MouseHit(1) Then
		Local MY = MouseY()
		Local Nr = (MY - MouseZ()*10)/30
		If Nr=>0 And Nr<FileNr Then 
			Ph_ReadFromFile("Setups\" + Files[Nr] + ".phS")
			Exit
		EndIf
	EndIf
	
	If KeyHit(1) Then End
	Flip
Forever
;~IDEal Editor Parameters:
;~C#Blitz3D