Function GUI()
	Local mx,my
	Local Form$
	WaitKey()
	Repeat
		mx=MouseX()
		my=MouseY()
		Cls
		If MouseHit(1) Then
			Cls
			Flip
			Print"waehle die Form des Objekts!"
			Print"0 fuer Exit, 1 fuer Rechteck, 2 fuer Kreis"
			Input("Deine Wahl:")
		Until KeyHit (1)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D