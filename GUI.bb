Function GUI()
	Local mx,my
	Local Testobject1.Ph_Object = New Ph_Object
	Local Choice$
	Local Form$
	Local XSize$
	Local YSize$
	Local negXSize$
	Local negYSize$
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
			Choice$=Input("Deine Wahl:")
			Select Choice$
				Case "0" 
					Return
				Case "1"
					Testobject1.Ph_Object
					Testobject1\Pos[0] = mx
					Testobject1\Pos[1] = my
					Print"waehle die waagerechte Geschwindigkeit!"
					Choice$=Input("Deine Wahl:")
					Testobject1\Vel[0] = Choice$
					Print"waehle die senkrechte Geschwindigkeit!"
					Choice$=Input("Deine Wahl:")
					Testobject1\Vel[1] = Choice$
					Print"waehle die Masse des Objekts"
					Choice$=Input("Deine Wahl:")
					Testobject1\Mass = Choice$
					Print"waehle die Ausrichtung!"
					Choice$=Input("Deine Wahl:")
					Testobject1\Rot = Choice$
					Print"waehle das Trägheitsmoment!"
					Choice$=Input("Deine Wahl:")
					Testobject1\Rot = Choice$
					Print"waehle die x-Abmessung!"
					XSize$=Input("Deine Wahl:")/2
					negXSize$=XSize$*-1
					Print"waehle die y-Abmessung!"
					YSize$=Input("Deine Wahl:")/2
					negYSize$=YSize$*-1
					Sh_CreateSquare(negXSize$,negYSize$,XSize$,YSize$)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D